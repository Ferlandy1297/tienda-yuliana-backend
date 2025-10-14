package com.tiendayuliana.backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.tiendayuliana.backend.dto.promocion.PromocionCreateDTO;
import com.tiendayuliana.backend.dto.promocion.PromocionResponseDTO;
import com.tiendayuliana.backend.service.PromocionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/promociones")
@Validated
public class PromocionController {
    private final PromocionService service;
    public PromocionController(PromocionService service) { this.service = service; }

    @GetMapping
    @PreAuthorize("@roles.has('ADMIN') or @roles.has('SUPERVISOR')")
    public List<PromocionResponseDTO> listar() { return service.listar(); }

    @PostMapping
    @PreAuthorize("@roles.has('ADMIN')")
    public PromocionResponseDTO crear(@Valid @RequestBody PromocionCreateDTO dto) { return service.crear(dto); }
}
