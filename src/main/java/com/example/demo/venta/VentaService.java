package com.example.demo.venta;

import com.example.demo.cliente.Cliente;
import com.example.demo.cliente.ClienteRepository;
import com.example.demo.lote.Lote;
import com.example.demo.lote.LoteRepository;
import com.example.demo.pago.Pago;
import com.example.demo.pago.PagoRepository;
import com.example.demo.producto.Producto;
import com.example.demo.producto.ProductoRepository;
import com.example.demo.user.UsuarioSis;
import com.example.demo.user.UsuarioSisRepository;
import com.example.demo.venta.dto.VentaCreateRequest;
import com.example.demo.venta.dto.VentaCreateResponse;
import com.example.demo.venta.dto.VentaItemRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioSisRepository usuarioRepo;
    private final PagoRepository pagoRepository;
    private final VentaDetalleRepository ventaDetalleRepository;

    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository, LoteRepository loteRepository, ClienteRepository clienteRepository, UsuarioSisRepository usuarioRepo, PagoRepository pagoRepository, VentaDetalleRepository ventaDetalleRepository, com.example.demo.fiado.CuentaCorrienteRepository cuentaCorrienteRepository, com.example.demo.fiado.MovimientoCcRepository movimientoCcRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepo = usuarioRepo;
        this.pagoRepository = pagoRepository;
        this.ventaDetalleRepository = ventaDetalleRepository;
        this.cuentaCorrienteRepository = cuentaCorrienteRepository;
        this.movimientoCcRepository = movimientoCcRepository;
    }

    @Transactional
    public VentaCreateResponse registrarVenta(VentaCreateRequest req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("La venta requiere items");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UsuarioSis usuario = usuarioRepo.findByNombreUsuarioAndActivoTrue(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no válido"));

        Venta v = new Venta();
        v.setFechaHora(OffsetDateTime.now());
        v.setTipo(req.getTipo() == null ? "DETALLE" : req.getTipo());
        if (req.getIdCliente() != null) {
            Cliente c = clienteRepository.findById(req.getIdCliente())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            if ("MAYOREO".equalsIgnoreCase(v.getTipo()) && !Boolean.TRUE.equals(c.getEsMayorista())) {
                throw new IllegalArgumentException("Cliente no es mayorista");
            }
            v.setCliente(c);
        }
        v.setUsuario(usuario);

        BigDecimal total = BigDecimal.ZERO;
        v = ventaRepository.save(v); // to get id

        List<VentaDetalle> detallesCreados = new ArrayList<>();

        for (VentaItemRequest item : req.getItems()) {
            Producto producto = resolveProducto(item);
            if (producto.getStock() < item.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para producto id=" + producto.getIdProducto());
            }

            BigDecimal precioUnitario;
            if ("MAYOREO".equalsIgnoreCase(v.getTipo())) {
                if (item.getPrecioUnitario() == null) {
                    throw new IllegalArgumentException("precioUnitario requerido para venta MAYOREO");
                }
                precioUnitario = item.getPrecioUnitario();
            } else {
                precioUnitario = producto.getPrecioVenta();
            }

            int porVender = item.getCantidad();
            List<Lote> lotes = loteRepository.findByProductoOrderByFechaVencimientoAscIdLoteAsc(producto);
            for (Lote lote : lotes) {
                if (porVender <= 0) break;
                int disponible = lote.getCantidadDisponible() == null ? 0 : lote.getCantidadDisponible();
                if (disponible <= 0) continue;
                int usar = Math.min(disponible, porVender);

                VentaDetalle vd = new VentaDetalle();
                vd.setVenta(v);
                vd.setProducto(producto);
                vd.setLote(lote);
                vd.setCantidad(usar);
                vd.setPrecioUnitario(precioUnitario);
                vd.setDescuento(BigDecimal.ZERO);
                VentaDetalleId id = new VentaDetalleId(v.getIdVenta(), producto.getIdProducto(), lote.getIdLote());
                vd.setId(id);
                detallesCreados.add(vd);

                // Update lote stock
                lote.setCantidadDisponible(disponible - usar);
                porVender -= usar;
                total = total.add(precioUnitario.multiply(BigDecimal.valueOf(usar)));
            }

            if (porVender > 0) {
                throw new IllegalArgumentException("Stock por lote insuficiente para producto " + producto.getIdProducto());
            }

            // Update producto stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        // Persist detalles and lotes
        loteRepository.saveAll(detallesCreados.stream().map(VentaDetalle::getLote).distinct().toList());
        ventaDetalleRepository.saveAll(detallesCreados);

        v.setTotal(total);
        ventaRepository.save(v);

        VentaCreateResponse resp = new VentaCreateResponse();
        resp.setIdVenta(v.getIdVenta());
        resp.setTotal(total);

        if ("FIADO".equalsIgnoreCase(v.getTipo())) {
            // Venta a crédito: no pago, registrar cargo en cuenta corriente
            if (v.getCliente() == null) {
                throw new IllegalArgumentException("Venta FIADO requiere cliente");
            }
            // Validación crédito mínima: límite vs saldo + total
            var cliente = v.getCliente();
            java.math.BigDecimal limite = cliente.getLimiteCredito();
            if (limite == null) limite = java.math.BigDecimal.ZERO;
            // Obtener/crear cuenta corriente
            com.example.demo.fiado.CuentaCorriente cc = cuentaCorriente(cliente);
            java.math.BigDecimal nuevoSaldo = cc.getSaldo().add(total);
            if (limite.compareTo(java.math.BigDecimal.ZERO) > 0 && nuevoSaldo.compareTo(limite) > 0) {
                // Bloquear cliente y rechazar
                cliente.setEstadoCredito("BLOQUEADO");
                clienteRepository.save(cliente);
                throw new IllegalArgumentException("Límite de crédito excedido");
            }
            cc.setSaldo(nuevoSaldo);
            cc.setEstado(nuevoSaldo.compareTo(java.math.BigDecimal.ZERO) == 0 ? "AL_DIA" : "EN_MORA");
            // Movimiento CARGO
            com.example.demo.fiado.MovimientoCc mov = new com.example.demo.fiado.MovimientoCc();
            mov.setCuenta(cc);
            mov.setTipo("CARGO");
            mov.setMonto(total);
            mov.setReferencia("Venta id=" + v.getIdVenta());
            movimientoCcRepository.save(mov);
            resp.setCambio(java.math.BigDecimal.ZERO);
            return resp;
        } else {
            if (req.getPago() == null || req.getPago().getMontoEntregado() == null) {
                throw new IllegalArgumentException("Pago efectivo requerido");
            }
            if (req.getPago().getMontoEntregado().compareTo(total) < 0) {
                throw new IllegalArgumentException("Monto entregado insuficiente");
            }
            Pago pago = new Pago();
            pago.setVenta(v);
            pago.setMetodo("EFECTIVO");
            pago.setMontoEntregado(req.getPago().getMontoEntregado());
            pago.setDenominacionBillete(req.getPago().getDenominacionBillete());
            pago.setCambioCalculado(req.getPago().getMontoEntregado().subtract(total));
            pago.setFechaHora(OffsetDateTime.now());
            pagoRepository.save(pago);
            resp.setCambio(pago.getCambioCalculado());
            return resp;
        }
    }

    private Producto resolveProducto(VentaItemRequest item) {
        if (item.getIdProducto() != null) {
            return productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        }
        if (item.getCodigoBarras() != null && !item.getCodigoBarras().isBlank()) {
            return productoRepository.findByCodigoBarras(item.getCodigoBarras())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado por codigo_barras"));
        }
        throw new IllegalArgumentException("Se requiere idProducto o codigoBarras");
    }

    // Helpers for fiados
    private final com.example.demo.fiado.CuentaCorrienteRepository cuentaCorrienteRepository;
    private final com.example.demo.fiado.MovimientoCcRepository movimientoCcRepository;

    private com.example.demo.fiado.CuentaCorriente cuentaCorriente(Cliente cliente) {
        return cuentaCorrienteRepository.findByCliente(cliente)
                .orElseGet(() -> {
                    com.example.demo.fiado.CuentaCorriente cc = new com.example.demo.fiado.CuentaCorriente();
                    cc.setCliente(cliente);
                    cc.setSaldo(java.math.BigDecimal.ZERO);
                    cc.setEstado("AL_DIA");
                    return cuentaCorrienteRepository.save(cc);
                });
    }
}
