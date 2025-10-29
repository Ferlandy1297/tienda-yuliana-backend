package com.example.demo.reporte;

import com.example.demo.venta.Venta;
import com.example.demo.venta.VentaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    private final VentaRepository ventaRepository;

    private final ReporteNativeRepository reporteNativeRepository;

    public ReporteController(VentaRepository ventaRepository, ReporteNativeRepository reporteNativeRepository) {
        this.ventaRepository = ventaRepository;
        this.reporteNativeRepository = reporteNativeRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ventas")
    public ResponseEntity<Map<String, Object>> ventas(
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fin) {

        OffsetDateTime start;
        OffsetDateTime end;

        if (periodo != null) {
            LocalDate today = LocalDate.now(ZoneOffset.UTC);
            switch (periodo.toLowerCase()) {
                case "diario" -> {
                    start = today.atStartOfDay().atOffset(ZoneOffset.UTC);
                    end = today.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
                }
                case "quincenal" -> {
                    LocalDate half = today.minusDays(14);
                    start = half.atStartOfDay().atOffset(ZoneOffset.UTC);
                    end = today.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
                }
                case "mensual" -> {
                    LocalDate first = today.withDayOfMonth(1);
                    start = first.atStartOfDay().atOffset(ZoneOffset.UTC);
                    end = first.plusMonths(1).atStartOfDay().atOffset(ZoneOffset.UTC);
                }
                default -> throw new IllegalArgumentException("periodo inválido");
            }
        } else {
            if (inicio == null || fin == null) throw new IllegalArgumentException("inicio y fin requeridos si no se usa periodo");
            start = OffsetDateTime.parse(inicio);
            end = OffsetDateTime.parse(fin);
        }

        List<Venta> ventas = ventaRepository.findByFechaHoraBetween(start, end);
        BigDecimal total = ventaRepository.totalBetween(start, end);

        Map<String, Object> res = new HashMap<>();
        res.put("desde", start);
        res.put("hasta", end);
        res.put("cantidad", ventas.size());
        res.put("total", total);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/ventas.csv", produces = "text/csv")
    public String ventasCsv(@RequestParam String periodo) {
        var r = this.ventas(periodo, null, null).getBody();
        StringBuilder sb = new StringBuilder();
        sb.append("desde,hasta,cantidad,total\n");
        sb.append(r.get("desde")).append(',').append(r.get("hasta")).append(',').append(r.get("cantidad")).append(',').append(r.get("total")).append('\n');
        return sb.toString();
    }

    // Productos más vendidos en un rango
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/mas-vendidos")
    public ResponseEntity<Map<String, Object>> masVendidos(
            @RequestParam String inicio,
            @RequestParam String fin,
            @RequestParam(defaultValue = "10") int top) {
        var start = OffsetDateTime.parse(inicio);
        var end = OffsetDateTime.parse(fin);
        var page = org.springframework.data.domain.PageRequest.of(0, top);
        var items = reporteNativeRepository.topVendidos(start, end, page);
        Map<String, Object> res = new HashMap<>();
        res.put("desde", start);
        res.put("hasta", end);
        res.put("top", top);
        res.put("items", items);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/utilidades")
    public ResponseEntity<Map<String, Object>> utilidades(@RequestParam String inicio, @RequestParam String fin) {
        var start = OffsetDateTime.parse(inicio);
        var end = OffsetDateTime.parse(fin);
        var v = reporteNativeRepository.utilidades(start, end);
        java.math.BigDecimal utilidad = v.getTotalVentas().subtract(v.getCostoVentas());
        Map<String, Object> res = new HashMap<>();
        res.put("desde", start);
        res.put("hasta", end);
        res.put("ventas", v.getTotalVentas());
        res.put("costo", v.getCostoVentas());
        res.put("utilidad", utilidad);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ventas/serie")
    public ResponseEntity<Map<String, Object>> serie(@RequestParam String inicio, @RequestParam String fin, @RequestParam(defaultValue = "day") String granularidad) {
        var start = OffsetDateTime.parse(inicio);
        var end = OffsetDateTime.parse(fin);
        String gran = switch (granularidad.toLowerCase()) { case "mes", "month" -> "month"; default -> "day"; };
        var items = reporteNativeRepository.serie(start, end, gran);
        Map<String, Object> res = new HashMap<>();
        res.put("desde", start);
        res.put("hasta", end);
        res.put("granularidad", gran);
        res.put("items", items);
        return ResponseEntity.ok(res);
    }
}
