package com.tiendayuliana.backend.service;

import java.util.List;

import com.tiendayuliana.backend.dto.proveedor.ProveedorCreateDTO;
import com.tiendayuliana.backend.dto.proveedor.ProveedorResponseDTO;
import com.tiendayuliana.backend.dto.proveedor.ProveedorUpdateDTO;

public interface ProveedorService {
    List<ProveedorResponseDTO> listar();
    ProveedorResponseDTO getById(Integer id);
    ProveedorResponseDTO crear(ProveedorCreateDTO dto);
    ProveedorResponseDTO actualizar(Integer id, ProveedorUpdateDTO dto);
    void eliminar(Integer id);
}

