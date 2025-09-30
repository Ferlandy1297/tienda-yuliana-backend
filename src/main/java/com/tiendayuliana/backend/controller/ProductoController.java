package com.tiendayuliana.backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.ProductCreateDTO;
import com.tiendayuliana.backend.dto.ProductResponseDTO;
import com.tiendayuliana.backend.dto.ProductUpdateDTO;
import com.tiendayuliana.backend.service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> crear(@Valid @RequestBody ProductCreateDTO dto) {
        ProductResponseDTO created = productoService.crear(dto);
        return ResponseEntity
                .created(URI.create("/api/productos/" + created.idProducto()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/stock-bajo")
    public List<ProductResponseDTO> stockBajo() {
        return productoService.stockBajo();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> actualizar(@PathVariable Integer id,
                                                         @Valid @RequestBody ProductUpdateDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
