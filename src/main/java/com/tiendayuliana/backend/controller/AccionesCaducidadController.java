package com.tiendayuliana.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.caducidad.CaducidadAccionDTO;
import com.tiendayuliana.backend.service.CaducidadService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/acciones")
@Validated
public class AccionesCaducidadController {
    private final CaducidadService caducidadService;
    public AccionesCaducidadController(CaducidadService caducidadService){ this.caducidadService = caducidadService; }

    @PostMapping("/caducidad")
    public ResponseEntity<java.util.Map<String, Object>> aplicar(@Valid @RequestBody CaducidadAccionDTO dto) {
        // TODO: retornar DTO resultado cuando se implemente
        caducidadService.aplicar(dto);
        return ResponseEntity.ok(java.util.Map.of("status", "ACEPTADO"));
    }
}
