package com.tiendayuliana.backend.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.devolucion.DevolucionCreateDTO;
import com.tiendayuliana.backend.service.DevolucionProveedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/devoluciones")
@Validated
public class DevolucionProveedorController {
    private final DevolucionProveedorService service;
    public DevolucionProveedorController(DevolucionProveedorService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<java.util.Map<String, Object>> registrar(@Valid @RequestBody DevolucionCreateDTO dto) {
        Integer id = service.registrar(dto);
        return ResponseEntity.created(URI.create("/api/devoluciones/" + id))
                .body(java.util.Map.of("idDevolucion", id));
    }
}

