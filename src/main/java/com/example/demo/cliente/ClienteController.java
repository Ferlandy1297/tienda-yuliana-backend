package com.example.demo.cliente;

import com.example.demo.cliente.dto.ClienteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteRepository repo;

    public ClienteController(ClienteRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping
    public List<Cliente> listar() {
        return repo.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtener(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody ClienteRequest req) {
        if (req.getNombre() == null || req.getNombre().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Cliente c = new Cliente();
        c.setNombre(req.getNombre());
        c.setTelefono(req.getTelefono());
        c.setNit(req.getNit());
        c.setEsMayorista(req.getEsMayorista() != null ? req.getEsMayorista() : Boolean.FALSE);
        c.setLimiteCredito(req.getLimiteCredito() != null ? req.getLimiteCredito() : java.math.BigDecimal.ZERO);
        if (req.getEstadoCredito() != null) {
            if (!"ACTIVO".equalsIgnoreCase(req.getEstadoCredito()) && !"BLOQUEADO".equalsIgnoreCase(req.getEstadoCredito())) {
                return ResponseEntity.badRequest().build();
            }
            c.setEstadoCredito(req.getEstadoCredito().toUpperCase());
        }
        c.setActivo(req.getActivo() != null ? req.getActivo() : Boolean.TRUE);
        return ResponseEntity.ok(repo.save(c));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Integer id, @RequestBody ClienteRequest req) {
        if (req.getEstadoCredito() != null) {
            String ec = req.getEstadoCredito().toUpperCase();
            if (!ec.equals("ACTIVO") && !ec.equals("BLOQUEADO")) {
                return ResponseEntity.badRequest().build();
            }
        }
        return repo.findById(id).map(c -> {
            if (req.getNombre() != null) c.setNombre(req.getNombre());
            if (req.getTelefono() != null) c.setTelefono(req.getTelefono());
            if (req.getNit() != null) c.setNit(req.getNit());
            if (req.getEsMayorista() != null) c.setEsMayorista(req.getEsMayorista());
            if (req.getLimiteCredito() != null) c.setLimiteCredito(req.getLimiteCredito());
            if (req.getEstadoCredito() != null) c.setEstadoCredito(req.getEstadoCredito().toUpperCase());
            if (req.getActivo() != null) c.setActivo(req.getActivo());
            return ResponseEntity.ok(repo.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }
}
