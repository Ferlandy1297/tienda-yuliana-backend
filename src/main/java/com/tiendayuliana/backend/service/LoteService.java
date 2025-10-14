package com.tiendayuliana.backend.service;

import java.util.List;

import com.tiendayuliana.backend.dto.lote.LotePorVencerDTO;

public interface LoteService {
    // TODO: implementar consulta por días a vencer (CU-TY-009)
    List<LotePorVencerDTO> porVencer(int dias);
}

