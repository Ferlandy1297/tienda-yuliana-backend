package com.tiendayuliana.backend.service;

import java.util.List;

import com.tiendayuliana.backend.dto.ProductCreateDTO;
import com.tiendayuliana.backend.dto.ProductResponseDTO;
import com.tiendayuliana.backend.dto.ProductUpdateDTO;

public interface ProductoService {
    ProductResponseDTO crear(ProductCreateDTO dto);
    ProductResponseDTO getById(Integer idProducto);
    List<ProductResponseDTO> listar();
    List<ProductResponseDTO> stockBajo();
    ProductResponseDTO actualizar(Integer idProducto, ProductUpdateDTO dto);
    void eliminar(Integer idProducto); 
}
