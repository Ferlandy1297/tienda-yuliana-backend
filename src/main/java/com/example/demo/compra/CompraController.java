package com.example.demo.compra;

import com.example.demo.compra.dto.CompraCreateRequest;
import com.example.demo.compra.dto.PagoCompraRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
public class CompraController {
    private final CompraService compraService;
    private final CompraRepository compraRepository;

    public CompraController(CompraService compraService, CompraRepository compraRepository) {
        this.compraService = compraService;
        this.compraRepository = compraRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Compra> crear(@RequestBody CompraCreateRequest req) {
        return ResponseEntity.ok(compraService.registrarCompra(req));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/pago")
    public ResponseEntity<Compra> pagar(@RequestBody PagoCompraRequest req) {
        return ResponseEntity.ok(compraService.registrarPago(req));
    }

    // Reporte de compras por periodo
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reporte")
    public ResponseEntity<Map<String, Object>> reporte(@RequestParam String periodo) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        OffsetDateTime start;
        OffsetDateTime end;
        switch (periodo.toLowerCase()) {
            case "diario" -> {
                start = today.atStartOfDay().atOffset(ZoneOffset.UTC);
                end = today.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
            }
            case "quincenal" -> {
                start = today.minusDays(14).atStartOfDay().atOffset(ZoneOffset.UTC);
                end = today.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
            }
            case "mensual" -> {
                LocalDate first = today.withDayOfMonth(1);
                start = first.atStartOfDay().atOffset(ZoneOffset.UTC);
                end = first.plusMonths(1).atStartOfDay().atOffset(ZoneOffset.UTC);
            }
            default -> throw new IllegalArgumentException("periodo inválido");
        }
        List<Compra> compras = compraRepository.findByFechaHoraBetween(start, end);
        java.math.BigDecimal total = compraRepository.totalBetween(start, end);
        Map<String, Object> res = new HashMap<>();
        res.put("desde", start);
        res.put("hasta", end);
        res.put("cantidad", compras.size());
        res.put("total", total);
        return ResponseEntity.ok(res);
    }

    // Export CSV (abre en Excel)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/reporte.csv", produces = "text/csv")
    public String reporteCsv(@RequestParam String periodo) {
        var r = this.reporte(periodo).getBody();
        StringBuilder sb = new StringBuilder();
        sb.append("desde,hasta,cantidad,total\n");
        sb.append(r.get("desde")).append(',').append(r.get("hasta")).append(',').append(r.get("cantidad")).append(',').append(r.get("total")).append('\n');
        return sb.toString();
    }
}
