package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.caducidad.CaducidadAccionDTO;

public interface CaducidadService {
    // TODO: aplicar acciones de caducidad impactando stock y registrando merma/devolución (CU-TY-009)
    void aplicar(CaducidadAccionDTO dto);
}

