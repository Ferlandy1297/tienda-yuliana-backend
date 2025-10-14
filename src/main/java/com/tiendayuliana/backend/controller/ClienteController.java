package com.tiendayuliana.backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.tiendayuliana.backend.dto.cliente.ClienteCreateDTO;
import com.tiendayuliana.backend.dto.cliente.ClienteResponseDTO;
import com.tiendayuliana.backend.dto.cliente.ClienteUpdateDTO;
import com.tiendayuliana.backend.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
@Validated
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service = service; }

    @GetMapping
    @PreAuthorize("@roles.has('ADMIN') or @roles.has('EMPLEADO') or @roles.has('SUPERVISOR')")
    public List<ClienteResponseDTO> listar() { return service.listar(); }

    @GetMapping("/{id}")
    @PreAuthorize("@roles.has('ADMIN') or @roles.has('EMPLEADO') or @roles.has('SUPERVISOR')")
    public ClienteResponseDTO get(@PathVariable Integer id) { return service.getById(id); }

    @PostMapping
    @PreAuthorize("@roles.has('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteCreateDTO dto) {
        var created = service.crear(dto);
        return ResponseEntity.created(URI.create("/api/clientes/"+created.idCliente())).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@roles.has('ADMIN')")
    public ClienteResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody ClienteUpdateDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@roles.has('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

