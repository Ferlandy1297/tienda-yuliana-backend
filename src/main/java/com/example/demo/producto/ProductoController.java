package com.example.demo.producto;

import com.example.demo.producto.dto.ProductoCreateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    public ProductoController(ProductoService productoService, ProductoRepository productoRepository) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoCreateRequest req) {
        return ResponseEntity.ok(productoService.crearProducto(req));
    }

    // Alertas de stock bajo
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/alertas/stock-bajo")
    public List<Producto> alertasStockBajo() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getActivo() != null && p.getActivo() && p.getStock() != null && p.getStockMinimo() != null && p.getStock() <= p.getStockMinimo())
                .toList();
    }

    // Cambiar precio (promoción simple)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/precio")
    public ResponseEntity<Producto> actualizarPrecio(@PathVariable Integer id, @RequestBody java.util.Map<String, Object> body) {
        if (!body.containsKey("precioVenta")) return ResponseEntity.badRequest().build();
        java.math.BigDecimal precio = new java.math.BigDecimal(body.get("precioVenta").toString());
        return productoRepository.findById(id).map(p -> {
            p.setPrecioVenta(precio);
            return ResponseEntity.ok(productoRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Notificar stock bajo por correo si está configurado spring.mail y app.alert.email.to
    private final org.springframework.mail.javamail.JavaMailSender mailSender = null;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/alertas/stock-bajo/notificar")
    public ResponseEntity<?> notificarStockBajo(@org.springframework.beans.factory.annotation.Value("${app.alert.email.to:}") String emailTo) {
        var productos = alertasStockBajo();
        if (emailTo == null || emailTo.isBlank() || mailSender == null) {
            return ResponseEntity.accepted().body(java.util.Map.of("mensaje", "Email no configurado", "count", productos.size()));
        }
        String cuerpo = productos.stream().map(p -> p.getNombre() + " (" + p.getStock() + "/" + p.getStockMinimo() + ")").reduce("Stock bajo:\n", (a,b) -> a + "- " + b + "\n");
        org.springframework.mail.SimpleMailMessage msg = new org.springframework.mail.SimpleMailMessage();
        msg.setTo(emailTo);
        msg.setSubject("Alertas de stock bajo");
        msg.setText(cuerpo);
        mailSender.send(msg);
        return ResponseEntity.ok(java.util.Map.of("enviados", productos.size()));
    }
}
