package com.tiendayuliana.backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.venta.PagoCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaDetalleCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaListItemDTO;     
import com.tiendayuliana.backend.dto.venta.VentaResponseDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.exception.NotFoundException;
import com.tiendayuliana.backend.model.Cliente;
import com.tiendayuliana.backend.model.Lote;
import com.tiendayuliana.backend.model.Pago;
import com.tiendayuliana.backend.model.Producto;
import com.tiendayuliana.backend.model.UsuarioSis;
import com.tiendayuliana.backend.model.Venta;
import com.tiendayuliana.backend.model.VentaDetalle;
import com.tiendayuliana.backend.model.VentaDetalleId;
import com.tiendayuliana.backend.repository.ClienteRepository;
import com.tiendayuliana.backend.repository.LoteRepository;
import com.tiendayuliana.backend.repository.PagoRepository;
import com.tiendayuliana.backend.repository.ProductoRepository;
import com.tiendayuliana.backend.repository.UsuarioSisRepository;
import com.tiendayuliana.backend.repository.VentaDetalleRepository;
import com.tiendayuliana.backend.repository.VentaRepository;
import com.tiendayuliana.backend.service.VentaService;


@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private LoteRepository loteRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioSisRepository usuarioSisRepository;
    @Autowired
    private com.tiendayuliana.backend.repository.CuentaCorrienteRepository cuentaCorrienteRepository;
    @Autowired
    private com.tiendayuliana.backend.repository.MovimientoCcRepository movimientoCcRepository;

    // Helper para retorno de consumos FIFO
    private static record ConsumoFIFO(Lote lote, int tomado) {}

    @Override
    @Transactional
    public VentaResponseDTO crear(VentaCreateDTO dto) {

        // Validar tipo
        String tipo = dto.getTipo() == null ? "DETALLE" : dto.getTipo().toUpperCase().trim();
        if (!(tipo.equals("DETALLE") || tipo.equals("MAYOREO") || tipo.equals("FIADO"))) {
            throw new BadRequestException("Tipo de venta inválido");
        }

        // Usuario
        UsuarioSis usuario = usuarioSisRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Cliente (opcional, requerido si FIADO)
        Cliente cliente = null;
        if (dto.getIdCliente() != null) {
            cliente = clienteRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        }
        if (tipo.equals("FIADO") && cliente == null) {
            throw new BadRequestException("Ventas FIADO requieren cliente");
        }
        if (tipo.equals("MAYOREO")) {
            if (cliente == null) {
                throw new BadRequestException("Las ventas al por mayor requieren un cliente registrado");
            }
            if (!cliente.isEsMayorista()) {
                throw new BadRequestException("El cliente seleccionado no es mayorista");
            }
        }

        // Crear venta
        Venta venta = new Venta();
        venta.setTipo(tipo);
        venta.setTotal(BigDecimal.ZERO);
        venta.setCliente(cliente);
        venta.setUsuario(usuario);
        venta = ventaRepository.save(venta);

        // Detalles
        BigDecimal total = BigDecimal.ZERO;
        List<VentaResponseDTO.DetalleOut> detallesOut = new ArrayList<>();

        for (VentaDetalleCreateDTO it : dto.getDetalles()) {
            Producto prod = productoRepository.findById(it.getIdProducto())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + it.getIdProducto()));

            // Precio (usa DTO si viene; si no, precio_venta del producto). Ajusta por MAYOREO si aplica.
            BigDecimal precioBase = it.getPrecioUnitario() == null ? prod.getPrecioVenta() : it.getPrecioUnitario();
            BigDecimal precio = "MAYOREO".equalsIgnoreCase(tipo)
                    ? calcularPrecioMayoreo(prod, it.getCantidad())
                    : precioBase;

            if (it.getIdLote() != null) {
                // ------- CON LOTE EXPLÍCITO (UN SOLO DETALLE) -------
                Lote lote = loteRepository.findById(it.getIdLote())
                        .orElseThrow(() -> new NotFoundException("Lote no encontrado: " + it.getIdLote()));
                if (!lote.getProducto().getIdProducto().equals(prod.getIdProducto())) {
                    throw new BadRequestException("El lote no corresponde al producto");
                }
                if (lote.getCantidadDisponible() < it.getCantidad()) {
                    throw new BadRequestException("Stock insuficiente en el lote " + it.getIdLote());
                }
                // Descuentos de stock
                lote.setCantidadDisponible(lote.getCantidadDisponible() - it.getCantidad());
                loteRepository.save(lote);

                if (prod.getStock() < it.getCantidad()) {
                    throw new BadRequestException("Stock insuficiente del producto " + prod.getIdProducto());
                }
                prod.setStock(prod.getStock() - it.getCantidad());
                productoRepository.save(prod);

                // Persistir detalle con id_lote obligatorio
                VentaDetalleId detId = new VentaDetalleId();
                detId.setIdVenta(venta.getIdVenta());
                detId.setIdProducto(prod.getIdProducto());
                detId.setIdLote(lote.getIdLote());

                VentaDetalle det = new VentaDetalle();
                det.setId(detId);
                det.setVenta(venta);
                det.setProducto(prod);
                det.setLote(lote); // NO null
                det.setCantidad(it.getCantidad());
                det.setPrecioUnitario(precio);
                det.setDescuento(it.getDescuento() == null ? BigDecimal.ZERO : it.getDescuento());
                ventaDetalleRepository.save(det);

                BigDecimal linea = precio
                        .multiply(BigDecimal.valueOf(it.getCantidad()))
                        .subtract(det.getDescuento());
                total = total.add(linea);

                detallesOut.add(new VentaResponseDTO.DetalleOut(
                        prod.getIdProducto(),
                        lote.getIdLote(),
                        it.getCantidad(),
                        precio,
                        det.getDescuento()
                ));

            } else {
                // ------- SIN LOTE: APLICAR FIFO (VARIOS DETALLES, UNO POR LOTE CONSUMIDO) -------
                List<ConsumoFIFO> consumos = consumirFIFO(prod.getIdProducto(), it.getCantidad());

                for (ConsumoFIFO c : consumos) {
                    // id compuesto con id_lote OBLIGATORIO
                    VentaDetalleId detId = new VentaDetalleId();
                    detId.setIdVenta(venta.getIdVenta());
                    detId.setIdProducto(prod.getIdProducto());
                    detId.setIdLote(c.lote().getIdLote());

                    VentaDetalle det = new VentaDetalle();
                    det.setId(detId);
                    det.setVenta(venta);
                    det.setProducto(prod);
                    det.setLote(c.lote()); // NO null
                    det.setCantidad(c.tomado());
                    det.setPrecioUnitario(precio);
                    det.setDescuento(it.getDescuento() == null ? BigDecimal.ZERO : it.getDescuento()); // prorratea si quieres
                    ventaDetalleRepository.save(det);

                    BigDecimal linea = precio
                            .multiply(BigDecimal.valueOf(c.tomado()))
                            .subtract(det.getDescuento());
                    total = total.add(linea);

                    detallesOut.add(new VentaResponseDTO.DetalleOut(
                            prod.getIdProducto(),
                            c.lote().getIdLote(),
                            c.tomado(),
                            precio,
                            det.getDescuento()
                    ));
                }
            }
        }

        // Actualizar total
        venta.setTotal(total);
        venta = ventaRepository.save(venta);

        // Pago (opcional; requerido salvo FIADO)
        VentaResponseDTO.PagoOut pagoOut = null;
        if (dto.getPago() != null) {
            PagoCreateDTO p = dto.getPago();
            if (p.getMetodo() != null && !"EFECTIVO".equalsIgnoreCase(p.getMetodo())) {
                throw new BadRequestException("Solo se admiten pagos en efectivo");
            }
            if (p.getMontoEntregado().compareTo(total) < 0) {
                throw new BadRequestException("Monto entregado insuficiente");
            }
            BigDecimal cambio = p.getMontoEntregado().subtract(total);

            Pago pago = new Pago();
            pago.setVenta(venta);
            pago.setMetodo("EFECTIVO"); // DDL restringe a EFECTIVO
            pago.setMontoEntregado(p.getMontoEntregado());
            pago.setCambioCalculado(cambio);
            pago.setDenominacionBillete(p.getDenominacionBillete());
            pago.setFechaHora(LocalDateTime.now());
            pago = pagoRepository.save(pago);

            pagoOut = new VentaResponseDTO.PagoOut(
                    pago.getIdPago(), pago.getMetodo(),
                    pago.getMontoEntregado(), pago.getCambioCalculado()
            );
        } else if (!tipo.equals("FIADO")) {
            throw new BadRequestException("Pago requerido salvo para ventas FIADO");
        }

        // Ventas a crédito (FIADO): validar límite y registrar CARGO en cuenta corriente
        if ("FIADO".equalsIgnoreCase(tipo)) {
            var cuenta = cuentaCorrienteRepository.findByCliente_IdCliente(cliente.getIdCliente())
                    .orElseThrow(() -> new BadRequestException("Cliente sin cuenta corriente"));
            BigDecimal saldo = movimientoCcRepository.saldoActual(cuenta.getIdCuenta());
            BigDecimal limite = cliente.getLimiteCredito() == null ? BigDecimal.ZERO : cliente.getLimiteCredito();
            if (saldo.add(total).compareTo(limite) > 0) {
                throw new BadRequestException("Límite de crédito excedido");
            }
            com.tiendayuliana.backend.model.MovimientoCc mov = new com.tiendayuliana.backend.model.MovimientoCc();
            mov.setCuenta(cuenta);
            mov.setTipo("CARGO");
            mov.setMonto(total);
            mov.setReferencia("Venta " + venta.getIdVenta());
            movimientoCcRepository.save(mov);
        }

        return new VentaResponseDTO(
                venta.getIdVenta(),
                venta.getTipo(),
                venta.getCliente() == null ? null : venta.getCliente().getIdCliente(),
                venta.getUsuario().getIdUsuario(),
                venta.getFechaHora(),
                venta.getTotal(),
                detallesOut,
                pagoOut
        );
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponseDTO getById(Integer idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));

        var dets = ventaDetalleRepository.findAllByVenta_IdVenta(idVenta);
        var detallesOut = dets.stream().map(d ->
                new VentaResponseDTO.DetalleOut(
                        d.getProducto().getIdProducto(),
                        d.getLote() == null ? null : d.getLote().getIdLote(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getDescuento()
                )
        ).toList();

        var pagoOpt = pagoRepository.findFirstByVenta_IdVentaOrderByFechaHoraAsc(idVenta);
        var pagoOut = pagoOpt.map(p ->
                new VentaResponseDTO.PagoOut(
                        p.getIdPago(), p.getMetodo(),
                        p.getMontoEntregado(), p.getCambioCalculado()
                )
        ).orElse(null);

        return new VentaResponseDTO(
                venta.getIdVenta(),
                venta.getTipo(),
                venta.getCliente() == null ? null : venta.getCliente().getIdCliente(),
                venta.getUsuario().getIdUsuario(),
                venta.getFechaHora(),
                venta.getTotal(),
                detallesOut,
                pagoOut
        );
    }

    // ==== ADAPTACIÓN: Listado de ventas (resumen) ====
    @Override
    @Transactional(readOnly = true)
    public List<VentaListItemDTO> listar() {
        return ventaRepository.findAllByOrderByFechaHoraDesc()
                .stream()
                .map(v -> new VentaListItemDTO(
                        v.getIdVenta(),
                        v.getFechaHora(),
                        v.getTipo(),
                        v.getCliente() == null ? null : v.getCliente().getIdCliente(),
                        v.getUsuario() == null ? null : v.getUsuario().getIdUsuario(),
                        v.getTotal()
                ))
                .toList();
    }

    // ===================== Helpers =====================

    /** Consume stock por FIFO y devuelve los lotes y cantidades tomadas de cada uno. */
    private List<ConsumoFIFO> consumirFIFO(Integer idProducto, int cantidadSolicitada) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (producto.getStock() < cantidadSolicitada) {
            throw new BadRequestException("Stock insuficiente del producto " + idProducto);
        }

        List<Lote> lotes = loteRepository
                .findByProducto_IdProductoOrderByFechaVencimientoAsc(idProducto);

        int restante = cantidadSolicitada;
        List<ConsumoFIFO> consumos = new ArrayList<>();

        for (Lote lote : lotes) {
            if (restante == 0) break;
            int tomar = Math.min(lote.getCantidadDisponible(), restante);
            if (tomar > 0) {
                lote.setCantidadDisponible(lote.getCantidadDisponible() - tomar);
                loteRepository.save(lote);
                consumos.add(new ConsumoFIFO(lote, tomar));
                restante -= tomar;
            }
        }
        if (restante > 0) {
            throw new BadRequestException("Stock insuficiente por lotes (FIFO)");
        }

        // Actualiza stock global del producto
        producto.setStock(producto.getStock() - cantidadSolicitada);
        productoRepository.save(producto);

        return consumos;
    }

    /** Regla simple de mayoreo (ejemplo: 10% descuento si cantidad >= 10). */
    private BigDecimal calcularPrecioMayoreo(Producto prod, int cantidad) {
        if (cantidad >= 10) {
            return prod.getPrecioVenta().multiply(BigDecimal.valueOf(0.9));
        }
        return prod.getPrecioVenta();
    }

    @Override
    @Transactional
    public VentaResponseDTO registrarPago(Integer idVenta, PagoCreateDTO p) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));

        // Evitar doble pago por diseño actual (un pago por venta)
        pagoRepository.findFirstByVenta_IdVentaOrderByFechaHoraAsc(idVenta)
                .ifPresent(x -> { throw new BadRequestException("La venta ya tiene un pago registrado"); });

        if (p.getMetodo() != null && !"EFECTIVO".equalsIgnoreCase(p.getMetodo())) {
            throw new BadRequestException("método de pago no soportado: solo EFECTIVO");
        }

        if (p.getMontoEntregado() == null || p.getMontoEntregado().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Monto entregado inválido");
        }

        BigDecimal total = venta.getTotal();
        BigDecimal cambio = p.getMontoEntregado().subtract(total);
        if (cambio.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Monto entregado insuficiente");
        }

        // Validar denominaciones soportadas
        if (p.getDenominacionBillete() != null) {
            var denom = p.getDenominacionBillete();
            java.util.Set<BigDecimal> soportadas = java.util.Set.of(
                    new BigDecimal("0.50"),
                    new BigDecimal("1"), new BigDecimal("5"), new BigDecimal("10"),
                    new BigDecimal("20"), new BigDecimal("50"), new BigDecimal("100"), new BigDecimal("200")
            );
            if (soportadas.stream().noneMatch(d -> d.compareTo(denom) == 0)) {
                throw new BadRequestException("Denominación no soportada");
            }
            // montoEntregado debe ser múltiplo de la denominación
            BigDecimal[] div = p.getMontoEntregado().divideAndRemainder(denom);
            if (div[1].compareTo(BigDecimal.ZERO) != 0) {
                throw new BadRequestException("El monto entregado debe ser múltiplo de la denominación informada");
            }
        }

        Pago pago = new Pago();
        pago.setVenta(venta);
        pago.setMetodo("EFECTIVO");
        pago.setMontoEntregado(p.getMontoEntregado());
        pago.setCambioCalculado(cambio);
        pago.setDenominacionBillete(p.getDenominacionBillete());
        pago.setFechaHora(LocalDateTime.now());
        pago = pagoRepository.save(pago);

        var dets = ventaDetalleRepository.findAllByVenta_IdVenta(idVenta);
        var detallesOut = dets.stream().map(d ->
                new VentaResponseDTO.DetalleOut(
                        d.getProducto().getIdProducto(),
                        d.getLote() == null ? null : d.getLote().getIdLote(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getDescuento()
                )
        ).toList();

        var pagoOut = new VentaResponseDTO.PagoOut(
                pago.getIdPago(), pago.getMetodo(),
                pago.getMontoEntregado(), pago.getCambioCalculado()
        );

        return new VentaResponseDTO(
                venta.getIdVenta(),
                venta.getTipo(),
                venta.getCliente() == null ? null : venta.getCliente().getIdCliente(),
                venta.getUsuario().getIdUsuario(),
                venta.getFechaHora(),
                venta.getTotal(),
                detallesOut,
                pagoOut
        );
    }
}
