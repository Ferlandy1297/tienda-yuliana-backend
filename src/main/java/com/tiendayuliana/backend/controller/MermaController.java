package com.tiendayuliana.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.merma.MermaCreateDTO;
import com.tiendayuliana.backend.service.MermaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mermas")
@Validated
public class MermaController {
    private final MermaService service;
    public MermaController(MermaService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<java.util.Map<String, Object>> registrar(@Valid @RequestBody MermaCreateDTO dto) {
        service.registrar(dto);
        return ResponseEntity.ok(java.util.Map.of("status", "OK"));
    }
}

