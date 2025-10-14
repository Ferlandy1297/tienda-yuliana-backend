package com.tiendayuliana.backend.service;

import java.util.List;

import com.tiendayuliana.backend.dto.promocion.PromocionCreateDTO;
import com.tiendayuliana.backend.dto.promocion.PromocionResponseDTO;

public interface PromocionService {
    List<PromocionResponseDTO> listar();
    PromocionResponseDTO crear(PromocionCreateDTO dto);
    java.util.Optional<PromocionResponseDTO> activa();
}

