package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.cc.AbonoCreateDTO;

public interface CuentaCorrienteService {
    // TODO: validar límite de crédito y bloqueo por mora (CU-TY-008)
    void registrarAbono(Integer idCuenta, AbonoCreateDTO dto);
}

