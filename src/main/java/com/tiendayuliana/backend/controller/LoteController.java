package com.tiendayuliana.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.lote.LotePorVencerDTO;
import com.tiendayuliana.backend.service.LoteService;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {
    private final LoteService loteService;
    public LoteController(LoteService loteService) { this.loteService = loteService; }

    @GetMapping("/por-vencer")
    public List<LotePorVencerDTO> porVencer(@RequestParam(defaultValue = "30") int dias) {
        return loteService.porVencer(dias);
    }
}

