package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.merma.MermaCreateDTO;

public interface MermaService {
    // TODO: disminuir stock y registrar merma (CU-TY-010)
    void registrar(MermaCreateDTO dto);
}

