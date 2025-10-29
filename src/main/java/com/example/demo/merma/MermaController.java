package com.example.demo.merma;

import com.example.demo.lote.Lote;
import com.example.demo.lote.LoteRepository;
import com.example.demo.producto.Producto;
import com.example.demo.producto.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/mermas")
public class MermaController {
    private final MermaRepository mermaRepo;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;

    public MermaController(MermaRepository mermaRepo, ProductoRepository productoRepository, LoteRepository loteRepository) {
        this.mermaRepo = mermaRepo;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> body) {
        Integer idProducto = (Integer) body.get("idProducto");
        Integer idLote = body.get("idLote") != null ? (Integer) body.get("idLote") : null;
        Integer cantidad = (Integer) body.get("cantidad");
        String motivo = body.get("motivo").toString();
        String obs = body.get("observacion") != null ? body.get("observacion").toString() : null;
        if (idProducto == null || cantidad == null || motivo == null) return ResponseEntity.badRequest().build();
        Producto p = productoRepository.findById(idProducto).orElse(null);
        if (p == null) return ResponseEntity.notFound().build();
        Lote lote = null;
        if (idLote != null) {
            lote = loteRepository.findById(idLote).orElse(null);
            if (lote == null) return ResponseEntity.badRequest().build();
        }
        if (p.getStock() < cantidad) return ResponseEntity.badRequest().body("Stock insuficiente");

        Merma m = new Merma();
        m.setProducto(p);
        m.setLote(lote);
        m.setCantidad(cantidad);
        m.setMotivo(motivo.toUpperCase());
        m.setObservacion(obs);
        m.setCostoEstimado(p.getCostoActual().multiply(BigDecimal.valueOf(cantidad)));
        mermaRepo.save(m);

        p.setStock(p.getStock() - cantidad);
        productoRepository.save(p);
        if (lote != null && lote.getCantidadDisponible() != null) {
            lote.setCantidadDisponible(Math.max(0, lote.getCantidadDisponible() - cantidad));
            loteRepository.save(lote);
        }
        return ResponseEntity.ok(m);
    }
}

