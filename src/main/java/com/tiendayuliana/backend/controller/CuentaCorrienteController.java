package com.tiendayuliana.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.cc.AbonoCreateDTO;
import com.tiendayuliana.backend.service.CuentaCorrienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cuentas")
@Validated
public class CuentaCorrienteController {

    private final CuentaCorrienteService service;

    public CuentaCorrienteController(CuentaCorrienteService service) {
        this.service = service;
    }

    @PostMapping("/{id}/abonos")
    public ResponseEntity<java.util.Map<String, Object>> abonar(@PathVariable Integer id,
                                                                @Valid @RequestBody AbonoCreateDTO dto) {
        // TODO: definir respuesta adecuada (DTO)
        service.registrarAbono(id, dto);
        return ResponseEntity.ok(java.util.Map.of("status", "OK"));
    }
}

