package com.tiendayuliana.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.tiendayuliana.backend.dto.compra.CompraCreateDTO;
import com.tiendayuliana.backend.dto.compra.PagoCompraDTO;
import com.tiendayuliana.backend.service.CompraService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
@Validated
public class CompraController {

    private final CompraService compraService;

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody @jakarta.validation.Valid CompraCreateDTO dto) {
        Integer id = compraService.crearCompra(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("compraId", id));
    }

    @PostMapping("/{id}/pagos")
    public java.util.Map<String, String> pagar(@PathVariable Integer id, @RequestBody @jakarta.validation.Valid PagoCompraDTO dto) {
        compraService.pagar(id, dto);
        return java.util.Map.of("status", "OK");
    }
}