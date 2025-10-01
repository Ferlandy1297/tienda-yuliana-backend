package com.tiendayuliana.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.ProductCreateDTO;
import com.tiendayuliana.backend.dto.ProductResponseDTO;
import com.tiendayuliana.backend.dto.ProductUpdateDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.exception.NotFoundException;
import com.tiendayuliana.backend.model.Producto;
import com.tiendayuliana.backend.model.Proveedor;
import com.tiendayuliana.backend.repository.ProductoRepository;
import com.tiendayuliana.backend.repository.ProveedorRepository;
import com.tiendayuliana.backend.service.ProductoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    @Override
    @Transactional
    public ProductResponseDTO crear(ProductCreateDTO dto) {
        String codigoBarras = null;
        if (dto.getCodigoBarras() != null) {
            codigoBarras = dto.getCodigoBarras().trim();
            if (codigoBarras.isEmpty()) {
                codigoBarras = null;
            } else {
                String finalCodigo = codigoBarras;
                productoRepository.findByCodigoBarras(finalCodigo)
                        .ifPresent(p -> { throw new BadRequestException("El código de barras ya existe"); });
            }
        }
        Proveedor proveedor = null;
        if (dto.getIdProveedor() != null) {
            proveedor = proveedorRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new NotFoundException("Proveedor no encontrado"));
        }
        Producto p = new Producto();
        p.setNombre(dto.getNombre().trim());
        p.setCodigoBarras(codigoBarras);
        p.setPrecioVenta(dto.getPrecioVenta());
        p.setCostoActual(dto.getCostoActual());
        p.setStock(dto.getStock());
        p.setStockMinimo(dto.getStockMinimo());
        p.setProveedor(proveedor);
        p.setActivo(true);
        p = productoRepository.save(p);
        return map(p);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getById(Integer idProducto) {
        Producto p = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        return map(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> listar() {
        return productoRepository.findAll()
                .stream().map(this::map).toList();
    }

    @Override
    @Transactional
    public ProductResponseDTO actualizar(Integer idProducto, ProductUpdateDTO dto) {
        Producto p = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        // validar unicidad de código de barras si cambia y no es vacío
        if (dto.getCodigoBarras() != null && !dto.getCodigoBarras().isBlank()) {
            var cb = dto.getCodigoBarras().trim();
            productoRepository.findByCodigoBarras(cb)
                    .filter(x -> !x.getIdProducto().equals(idProducto))
                    .ifPresent(x -> { throw new BadRequestException("El código de barras ya existe"); });
            p.setCodigoBarras(cb);
        } else {
            p.setCodigoBarras(null);
        }

        p.setNombre(dto.getNombre().trim());
        p.setPrecioVenta(dto.getPrecioVenta());
        p.setCostoActual(dto.getCostoActual());
        p.setStock(dto.getStock());
        p.setStockMinimo(dto.getStockMinimo());

        if (dto.getIdProveedor() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new NotFoundException("Proveedor no encontrado"));
            p.setProveedor(proveedor);
        } else {
            p.setProveedor(null);
        }

        if (dto.getActivo() != null) {
            p.setActivo(dto.getActivo());
        }

        p = productoRepository.save(p);
        return map(p);
    }

    @Override
    @Transactional
    public void eliminar(Integer idProducto) {
        // Físico:
        if (!productoRepository.existsById(idProducto)) {
            throw new NotFoundException("Producto no encontrado");
        }
        productoRepository.deleteById(idProducto);

        
    }

    private ProductResponseDTO map(Producto p) {
        return new ProductResponseDTO(
                p.getIdProducto(),
                p.getNombre(),
                p.getCodigoBarras(),
                p.getPrecioVenta(),
                p.getCostoActual(),
                p.getStock(),
                p.getStockMinimo(),
                p.getProveedor()!=null? p.getProveedor().getIdProveedor(): null,
                p.getActivo()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> stockBajo() {
        return productoRepository.findStockBajo().stream().map(this::map).toList();
    }

}
