package com.tiendayuliana.backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.compra.CompraCreateDTO;
import com.tiendayuliana.backend.dto.compra.CompraItemDTO;
import com.tiendayuliana.backend.dto.compra.PagoCompraDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.exception.NotFoundException;
import com.tiendayuliana.backend.model.Compra;
import com.tiendayuliana.backend.model.CompraDetalle;
import com.tiendayuliana.backend.model.CompraDetalleId;
import com.tiendayuliana.backend.model.Lote;
import com.tiendayuliana.backend.model.PagoCompra;
import com.tiendayuliana.backend.model.Producto;
import com.tiendayuliana.backend.model.Proveedor;
import com.tiendayuliana.backend.repository.CompraDetalleRepository;
import com.tiendayuliana.backend.repository.CompraRepository;
import com.tiendayuliana.backend.repository.LoteRepository;
import com.tiendayuliana.backend.repository.PagoCompraRepository;
import com.tiendayuliana.backend.repository.ProductoRepository;
import com.tiendayuliana.backend.repository.ProveedorRepository;
import com.tiendayuliana.backend.service.CompraService;

import lombok.RequiredArgsConstructor;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepo;
    @Autowired
    private CompraDetalleRepository compraDetRepo;
    @Autowired
    private PagoCompraRepository pagoCompraRepo;
    @Autowired
    private ProveedorRepository proveedorRepo;
    @Autowired
    private ProductoRepository productoRepo;
    @Autowired
    private LoteRepository loteRepo;
    @Override
    @Transactional
    public Integer crearCompra(CompraCreateDTO dto) {
        Proveedor prov = proveedorRepo.findById(dto.idProveedor())
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado"));

        Compra compra = new Compra();
        compra.setProveedor(prov);
        compra.setCondicion(dto.condicion() == null ? "CONTADO" : dto.condicion().toUpperCase());
        compra.setEstado("ABIERTA");
        compra.setObservacion(dto.observacion());
        compra.setTotal(BigDecimal.ZERO);
        compra = compraRepo.save(compra); // obtener idCompra antes de los detalles

        BigDecimal total = BigDecimal.ZERO;

        for (CompraItemDTO it : dto.items()) {
            Producto prod = productoRepo.findById(it.idProducto())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + it.idProducto()));

            if (it.cantidad() <= 0) {
                throw new BadRequestException("Cantidad inválida");
            }
            if (it.costoUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Costo inválido");
            }

            // Crear lote
            Lote lote = new Lote(null, prod, it.fechaVencimiento(), it.cantidad());
            lote = loteRepo.save(lote);

            // Actualizar stock del producto
            prod.setStock(prod.getStock() + it.cantidad());
            productoRepo.save(prod);

            // Crear detalle (id compuesto completo)
            CompraDetalleId detId = new CompraDetalleId();
            detId.setIdCompra(compra.getIdCompra());
            detId.setIdProducto(prod.getIdProducto());
            detId.setIdLote(lote.getIdLote());
            CompraDetalle det = new CompraDetalle();
            det.setId(detId);
            det.setCompra(compra);
            det.setProducto(prod);
            det.setLote(lote);
            det.setCantidad(it.cantidad());
            det.setCostoUnitario(it.costoUnitario());
            det.setFechaVencimiento(it.fechaVencimiento());
            compraDetRepo.save(det);

            total = total.add(it.costoUnitario().multiply(BigDecimal.valueOf(it.cantidad())));
        }

        compra.setTotal(total);
        compraRepo.save(compra);

        // Pago inicial (opcional)
        if (dto.pagoInicial() != null && dto.pagoInicial().compareTo(BigDecimal.ZERO) > 0) {
            if (dto.pagoInicial().compareTo(total) > 0) {
                throw new BadRequestException("Pago inicial mayor al total");
            }
            PagoCompra pago = new PagoCompra();
            pago.setCompra(compra);
            pago.setMetodo("EFECTIVO");
            pago.setMonto(dto.pagoInicial());
            pagoCompraRepo.save(pago);
        }

        // Actualizar estado
        BigDecimal pagado = pagoCompraRepo.sumByCompraId(compra.getIdCompra()).orElse(BigDecimal.ZERO);
        if (pagado.compareTo(BigDecimal.ZERO) == 0) {
            compra.setEstado("ABIERTA");
        } else if (pagado.compareTo(total) < 0) {
            compra.setEstado("PARCIAL");
        } else {
            compra.setEstado("PAGADA");
        }
        compraRepo.save(compra);

        return compra.getIdCompra();
    }

    @Override
    @Transactional
    public void pagar(Integer idCompra, PagoCompraDTO dto) {
        Compra compra = compraRepo.findById(idCompra)
                .orElseThrow(() -> new NotFoundException("Compra no encontrada"));

        if (dto.monto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Monto inválido");
        }

        PagoCompra pago = new PagoCompra();
        pago.setCompra(compra);
        pago.setMetodo("EFECTIVO");
        pago.setMonto(dto.monto());
        pago.setObservacion(dto.observacion());
        pagoCompraRepo.save(pago);

        BigDecimal pagado = pagoCompraRepo.sumByCompraId(idCompra).orElse(BigDecimal.ZERO);
        if (pagado.compareTo(compra.getTotal()) >= 0) {
            compra.setEstado("PAGADA");
        } else if (pagado.compareTo(BigDecimal.ZERO) > 0) {
            compra.setEstado("PARCIAL");
        } else {
            compra.setEstado("ABIERTA");
        }
        compraRepo.save(compra);
    }
}