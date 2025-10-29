package com.example.demo.lote;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {
    private final LoteRepository loteRepository;

    public LoteController(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/por-vencer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Lote>> porVencer(@RequestParam(defaultValue = "30") int dias) {
        LocalDate limite = LocalDate.now().plusDays(dias);
        List<Lote> list = loteRepository.findAll().stream()
                .filter(l -> l.getFechaVencimiento() != null)
                .filter(l -> l.getCantidadDisponible() != null && l.getCantidadDisponible() > 0)
                .filter(l -> !l.getFechaVencimiento().isAfter(limite))
                .toList();
        return ResponseEntity.ok(list);
    }
}

