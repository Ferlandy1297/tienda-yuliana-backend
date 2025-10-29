package com.example.demo.compra;

import com.example.demo.lote.Lote;
import com.example.demo.lote.LoteRepository;
import com.example.demo.producto.Producto;
import com.example.demo.producto.ProductoRepository;
import com.example.demo.producto.Proveedor;
import com.example.demo.producto.ProveedorRepository;
import com.example.demo.compra.dto.CompraCreateRequest;
import com.example.demo.compra.dto.CompraItemRequest;
import com.example.demo.compra.dto.PagoCompraRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompraService {
    private final CompraRepository compraRepository;
    private final CompraDetalleRepository compraDetalleRepository;
    private final PagoCompraRepository pagoCompraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;

    public CompraService(CompraRepository compraRepository, CompraDetalleRepository compraDetalleRepository, PagoCompraRepository pagoCompraRepository, ProveedorRepository proveedorRepository, ProductoRepository productoRepository, LoteRepository loteRepository) {
        this.compraRepository = compraRepository;
        this.compraDetalleRepository = compraDetalleRepository;
        this.pagoCompraRepository = pagoCompraRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
    }

    @Transactional
    public Compra registrarCompra(CompraCreateRequest req) {
        if (req.getIdProveedor() == null || req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("Proveedor e items requeridos");
        }
        Proveedor prov = proveedorRepository.findById(req.getIdProveedor()).orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        Compra compra = new Compra();
        compra.setProveedor(prov);
        compra.setFechaHora(OffsetDateTime.now());
        compra.setCondicion(req.getCondicion() != null ? req.getCondicion().toUpperCase() : "CONTADO");
        compra.setEstado(req.getEstado() != null ? req.getEstado().toUpperCase() : "ABIERTA");
        compra.setObservacion(req.getObservacion());
        compra = compraRepository.save(compra);

        BigDecimal total = BigDecimal.ZERO;
        List<CompraDetalle> detalles = new ArrayList<>();
        for (CompraItemRequest it : req.getItems()) {
            Producto prod = productoRepository.findById(it.getIdProducto()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            Lote lote = new Lote();
            lote.setProducto(prod);
            lote.setFechaVencimiento(it.getFechaVencimiento());
            lote.setCantidadDisponible(it.getCantidad());
            lote = loteRepository.save(lote);

            CompraDetalle cd = new CompraDetalle();
            cd.setCompra(compra);
            cd.setProducto(prod);
            cd.setLote(lote);
            cd.setCantidad(it.getCantidad());
            cd.setCostoUnitario(it.getCostoUnitario());
            cd.setFechaVencimiento(it.getFechaVencimiento());
            cd.setId(new CompraDetalleId(compra.getIdCompra(), prod.getIdProducto(), lote.getIdLote()));
            detalles.add(cd);

            // actualizar stock y costo del producto
            prod.setStock((prod.getStock() == null ? 0 : prod.getStock()) + it.getCantidad());
            prod.setCostoActual(it.getCostoUnitario());
            productoRepository.save(prod);

            total = total.add(it.getCostoUnitario().multiply(BigDecimal.valueOf(it.getCantidad())));
        }
        compraDetalleRepository.saveAll(detalles);
        compra.setTotal(total);
        compraRepository.save(compra);
        return compra;
    }

    @Transactional
    public Compra registrarPago(PagoCompraRequest req) {
        Compra compra = compraRepository.findById(req.getIdCompra()).orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));
        PagoCompra pago = new PagoCompra();
        pago.setCompra(compra);
        pago.setMetodo(req.getMetodo() != null ? req.getMetodo().toUpperCase() : "EFECTIVO");
        pago.setMonto(req.getMonto());
        pago.setObservacion(req.getObservacion());
        pago.setFechaHora(OffsetDateTime.now());
        pagoCompraRepository.save(pago);

        // Calcular saldo pagado
        java.math.BigDecimal pagado = pagoCompraRepository.findAll().stream()
                .filter(p -> p.getCompra().getIdCompra().equals(compra.getIdCompra()))
                .map(PagoCompra::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (pagado.compareTo(compra.getTotal()) >= 0) {
            compra.setEstado("PAGADA");
        } else if (pagado.compareTo(BigDecimal.ZERO) > 0) {
            compra.setEstado("PARCIAL");
        } else {
            compra.setEstado("ABIERTA");
        }
        compraRepository.save(compra);
        return compra;
    }
}
