package com.example.demo.venta;

import com.example.demo.venta.dto.VentaCreateRequest;
import com.example.demo.venta.dto.VentaCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Registrar venta (detalle / mayoreo / fiado), descuenta stock y registra pago efectivo
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping
    public ResponseEntity<VentaCreateResponse> crear(@RequestBody VentaCreateRequest req) {
        return ResponseEntity.ok(ventaService.registrarVenta(req));
    }
}

