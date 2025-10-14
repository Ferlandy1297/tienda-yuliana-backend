package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.devolucion.DevolucionCreateDTO;

public interface DevolucionProveedorService {
    // TODO: afectar stock y crear entidades de devolución (CU-TY-011)
    Integer registrar(DevolucionCreateDTO dto);
}

