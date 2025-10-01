package com.tiendayuliana.backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.venta.VentaCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaListItemDTO;
import com.tiendayuliana.backend.dto.venta.VentaResponseDTO;
import com.tiendayuliana.backend.service.VentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crear(@Valid @RequestBody VentaCreateDTO dto) {
        VentaResponseDTO created = ventaService.crear(dto);
        return ResponseEntity.created(URI.create("/api/ventas/" + created.idVenta()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<VentaListItemDTO>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }
    
}