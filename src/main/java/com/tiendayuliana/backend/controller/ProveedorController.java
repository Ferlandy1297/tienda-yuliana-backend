package com.tiendayuliana.backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.tiendayuliana.backend.dto.proveedor.ProveedorCreateDTO;
import com.tiendayuliana.backend.dto.proveedor.ProveedorResponseDTO;
import com.tiendayuliana.backend.dto.proveedor.ProveedorUpdateDTO;
import com.tiendayuliana.backend.service.ProveedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/proveedores")
@Validated
public class ProveedorController {
    private final ProveedorService service;
    public ProveedorController(ProveedorService service) { this.service = service; }

    @GetMapping
    @PreAuthorize("@roles.has('ADMIN') or @roles.has('EMPLEADO') or @roles.has('SUPERVISOR')")
    public List<ProveedorResponseDTO> listar() { return service.listar(); }

    @GetMapping("/{id}")
    @PreAuthorize("@roles.has('ADMIN') or @roles.has('EMPLEADO') or @roles.has('SUPERVISOR')")
    public ProveedorResponseDTO get(@PathVariable Integer id) { return service.getById(id); }

    @PostMapping
    @PreAuthorize("@roles.has('ADMIN')")
    public ResponseEntity<ProveedorResponseDTO> crear(@Valid @RequestBody ProveedorCreateDTO dto) {
        var created = service.crear(dto);
        return ResponseEntity.created(URI.create("/api/proveedores/"+created.idProveedor())).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@roles.has('ADMIN')")
    public ProveedorResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody ProveedorUpdateDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@roles.has('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

