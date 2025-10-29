package com.example.demo.producto;

import com.example.demo.producto.dto.ProveedorRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {
    private final ProveedorRepository repo;

    public ProveedorController(ProveedorRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping
    public List<Proveedor> listar() {
        return repo.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtener(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody ProveedorRequest req) {
        if (req.getNombre() == null || req.getNombre().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Proveedor p = new Proveedor();
        p.setNombre(req.getNombre());
        p.setContacto(req.getContacto());
        p.setTelefono(req.getTelefono());
        p.setDireccion(req.getDireccion());
        p.setActivo(req.getActivo() != null ? req.getActivo() : Boolean.TRUE);
        return ResponseEntity.ok(repo.save(p));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Integer id, @RequestBody ProveedorRequest req) {
        return repo.findById(id).map(p -> {
            if (req.getNombre() != null) p.setNombre(req.getNombre());
            if (req.getContacto() != null) p.setContacto(req.getContacto());
            if (req.getTelefono() != null) p.setTelefono(req.getTelefono());
            if (req.getDireccion() != null) p.setDireccion(req.getDireccion());
            if (req.getActivo() != null) p.setActivo(req.getActivo());
            return ResponseEntity.ok(repo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }
}

