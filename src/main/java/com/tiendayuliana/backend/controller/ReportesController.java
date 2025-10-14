package com.tiendayuliana.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.reportes.ReporteComprasDTO;
import com.tiendayuliana.backend.dto.reportes.ReporteVentasDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.service.ReporteComprasService;
import com.tiendayuliana.backend.service.ReporteService;


@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ReporteService reporteService;
    private final ReporteComprasService reporteComprasService;

    public ReportesController(ReporteService reporteService, ReporteComprasService reporteComprasService) {
        this.reporteService = reporteService;
        this.reporteComprasService = reporteComprasService;
    }

    @GetMapping("/ventas")
    public ReporteVentasDTO ventas(@RequestParam String periodo) {
        return reporteService.ventasPorPeriodo(periodo);
    }

    @GetMapping("/compras")
    public List<ReporteComprasDTO> compras(@RequestParam LocalDate desde,
                                           @RequestParam LocalDate hasta,
                                           @RequestParam(name = "proveedorId", required = false) Integer proveedorId) {
        if (hasta.isBefore(desde)) {
            throw new BadRequestException("La fecha 'hasta' no puede ser anterior a 'desde'");
        }
        return reporteComprasService.compras(desde, hasta, proveedorId);
    }
}
