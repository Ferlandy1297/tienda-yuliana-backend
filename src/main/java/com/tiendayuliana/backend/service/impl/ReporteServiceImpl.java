package com.tiendayuliana.backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.reportes.ReporteVentasDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.repository.VentaRepository;
import com.tiendayuliana.backend.service.ReporteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;

    @Override
    @Transactional(readOnly = true)
    public ReporteVentasDTO ventasPorPeriodo(String periodo) {
        String normalized = periodo == null ? "DIARIO" : periodo.trim().toUpperCase();
        LocalDate hoy = LocalDate.now();
        LocalDate inicio;
        switch (normalized) {
            case "DIARIO" -> inicio = hoy;
            case "QUINCENAL" -> inicio = hoy.minusDays(14);
            case "MENSUAL" -> inicio = hoy.minusDays(29);
            default -> throw new BadRequestException("Periodo inválido. Use diario, quincenal o mensual");
        }

        LocalDateTime inicioDt = inicio.atStartOfDay();
        LocalDateTime finDt = hoy.plusDays(1).atStartOfDay().minusNanos(1);

        List<ReporteVentasDTO.DetalleDia> detalle = ventaRepository.ventasPorPeriodo(inicioDt, finDt)
                .stream()
                .map(row -> new ReporteVentasDTO.DetalleDia(
                        toLocalDate(row[0]),
                        row[1] == null ? BigDecimal.ZERO : (BigDecimal) row[1],
                        ((Number) row[2]).longValue()))
                .toList();

        BigDecimal total = ventaRepository.totalEntreFechas(inicioDt, finDt);
        long cantidad = ventaRepository.totalVentasEntreFechas(inicioDt, finDt);

        return new ReporteVentasDTO(normalized, inicio, hoy, total, cantidad, detalle);
    }

    private LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate date) {
            return date;
        }
        if (value instanceof java.sql.Date date) {
            return date.toLocalDate();
        }
        throw new IllegalStateException("No se pudo convertir la fecha del reporte");
    }
}
