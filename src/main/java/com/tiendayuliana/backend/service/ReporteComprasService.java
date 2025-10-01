package com.tiendayuliana.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tiendayuliana.backend.dto.reportes.ReporteComprasDTO;
import com.tiendayuliana.backend.repository.CompraRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteComprasService {
    private final CompraRepository compraRepository;

    public List<ReporteComprasDTO> compras(LocalDate desde, LocalDate hasta, Integer proveedorId) {
        LocalDateTime ini = desde.atStartOfDay();
        LocalDateTime fin = hasta.plusDays(1).atStartOfDay().minusNanos(1);
        return compraRepository.comprasPorPeriodo(ini, fin, proveedorId)
                .stream()
                .map(r -> new ReporteComprasDTO(toLocalDate(r[0]), (BigDecimal) r[1]))
                .toList();
    }

    private LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate date) {
            return date;
        }
        if (value instanceof java.sql.Date date) {
            return date.toLocalDate();
        }
        throw new IllegalStateException("No se pudo convertir la fecha del reporte de compras");
    }
}
