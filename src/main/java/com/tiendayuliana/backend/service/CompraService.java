package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.compra.CompraCreateDTO;
import com.tiendayuliana.backend.dto.compra.PagoCompraDTO;

public interface CompraService {
    Integer crearCompra(CompraCreateDTO dto);
    void pagar(Integer idCompra, PagoCompraDTO dto);
}
