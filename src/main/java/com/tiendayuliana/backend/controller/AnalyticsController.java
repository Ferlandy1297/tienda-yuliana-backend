package com.tiendayuliana.backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.repository.VentaDetalleRepository;
import com.tiendayuliana.backend.repository.VentaRepository;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository detalleRepository;
    public AnalyticsController(VentaRepository ventaRepository, VentaDetalleRepository detalleRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleRepository = detalleRepository;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(@RequestParam LocalDate desde, @RequestParam LocalDate hasta) {
        var ini = desde.atStartOfDay();
        var fin = hasta.plusDays(1).atStartOfDay().minusNanos(1);
        var ventasPorDia = ventaRepository.ventasPorPeriodo(ini, fin);
        BigDecimal total = ventaRepository.totalEntreFechas(ini, fin);
        long cantVentas = ventaRepository.totalVentasEntreFechas(ini, fin);
        var dets = detalleRepository.findAllBetween(ini, fin);
        BigDecimal utilidad = BigDecimal.ZERO;
        long items = 0;
        for (var d : dets) {
            var costo = d.getProducto().getCostoActual() == null ? BigDecimal.ZERO : d.getProducto().getCostoActual();
            var linea = d.getPrecioUnitario().subtract(costo)
                    .multiply(BigDecimal.valueOf(d.getCantidad()))
                    .subtract(d.getDescuento() == null ? BigDecimal.ZERO : d.getDescuento());
            utilidad = utilidad.add(linea);
            items += d.getCantidad();
        }
        BigDecimal ticketsProm = cantVentas == 0 ? BigDecimal.ZERO : total.divide(BigDecimal.valueOf(cantVentas), java.math.RoundingMode.HALF_UP);
        return Map.of(
                "ventasPorDia", ventasPorDia,
                "totalVentas", total,
                "totalTransacciones", cantVentas,
                "utilidad", utilidad,
                "ticketsPromedio", ticketsProm
        );
    }
}

