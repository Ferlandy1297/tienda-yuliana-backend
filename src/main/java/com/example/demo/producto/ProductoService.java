package com.example.demo.producto;

import com.example.demo.producto.dto.ProductoCreateRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoService(ProductoRepository productoRepository, ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional
    public Producto crearProducto(ProductoCreateRequest req) {
        if (req.getCodigoBarras() != null && !req.getCodigoBarras().isBlank()) {
            if (productoRepository.existsByCodigoBarras(req.getCodigoBarras())) {
                throw new IllegalArgumentException("codigo_barras ya existe");
            }
        }
        Producto p = new Producto();
        p.setNombre(req.getNombre());
        p.setCodigoBarras(req.getCodigoBarras());
        p.setPrecioVenta(req.getPrecioVenta());
        p.setCostoActual(req.getCostoActual() == null ? java.math.BigDecimal.ZERO : req.getCostoActual());
        p.setStock(req.getStock() == null ? 0 : req.getStock());
        p.setStockMinimo(req.getStockMinimo() == null ? 0 : req.getStockMinimo());
        if (req.getIdProveedor() != null) {
            Proveedor prov = proveedorRepository.findById(req.getIdProveedor())
                    .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
            p.setProveedor(prov);
        }
        return productoRepository.save(p);
    }
}

