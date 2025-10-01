package com.tiendayuliana.backend.service;

import java.util.List;

import com.tiendayuliana.backend.dto.venta.VentaCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaListItemDTO;
import com.tiendayuliana.backend.dto.venta.VentaResponseDTO;

public interface VentaService {
    VentaResponseDTO crear(VentaCreateDTO dto);
    VentaResponseDTO getById(Integer idVenta);
    List<VentaListItemDTO> listar();  
}
