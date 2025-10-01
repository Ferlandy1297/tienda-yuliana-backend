package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.reportes.ReporteVentasDTO;

public interface ReporteService {
    ReporteVentasDTO ventasPorPeriodo(String periodo);
}
