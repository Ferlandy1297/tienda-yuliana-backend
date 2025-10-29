package com.example.demo.devolucion;

import com.example.demo.lote.Lote;
import com.example.demo.lote.LoteRepository;
import com.example.demo.producto.Producto;
import com.example.demo.producto.ProductoRepository;
import com.example.demo.producto.Proveedor;
import com.example.demo.producto.ProveedorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devoluciones-proveedor")
public class DevolucionController {
    private final DevolucionProveedorRepository devRepo;
    private final DevolucionDetalleRepository detRepo;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;

    public DevolucionController(DevolucionProveedorRepository devRepo, DevolucionDetalleRepository detRepo, ProveedorRepository proveedorRepository, ProductoRepository productoRepository, LoteRepository loteRepository) {
        this.devRepo = devRepo;
        this.detRepo = detRepo;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        Integer idProveedor = (Integer) body.get("idProveedor");
        String motivo = body.get("motivo") != null ? body.get("motivo").toString() : null;
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        if (idProveedor == null || items == null || items.isEmpty()) return ResponseEntity.badRequest().build();
        Proveedor prov = proveedorRepository.findById(idProveedor).orElse(null);
        if (prov == null) return ResponseEntity.badRequest().build();
        DevolucionProveedor d = new DevolucionProveedor();
        d.setProveedor(prov);
        d.setMotivo(motivo);
        d = devRepo.save(d);

        BigDecimal total = BigDecimal.ZERO;
        List<DevolucionDetalle> dets = new ArrayList<>();
        for (Map<String, Object> it : items) {
            Integer idProducto = (Integer) it.get("idProducto");
            Integer idLote = (Integer) it.get("idLote");
            Integer cantidad = (Integer) it.get("cantidad");
            BigDecimal costo = new BigDecimal(it.get("costoEstimado").toString());
            Producto p = productoRepository.findById(idProducto).orElse(null);
            Lote l = loteRepository.findById(idLote).orElse(null);
            if (p == null || l == null) return ResponseEntity.badRequest().build();
            if (l.getCantidadDisponible() < cantidad) return ResponseEntity.badRequest().body("Cantidad de lote insuficiente");

            DevolucionDetalle dd = new DevolucionDetalle();
            dd.setDevolucion(d);
            dd.setProducto(p);
            dd.setLote(l);
            dd.setCantidad(cantidad);
            dd.setCostoEstimado(costo);
            dd.setId(new DevolucionDetalleId(d.getIdDevolucion(), p.getIdProducto(), l.getIdLote()));
            dets.add(dd);

            p.setStock(p.getStock() - cantidad);
            productoRepository.save(p);
            l.setCantidadDisponible(l.getCantidadDisponible() - cantidad);
            loteRepository.save(l);
            total = total.add(costo.multiply(BigDecimal.valueOf(cantidad)));
        }
        detRepo.saveAll(dets);
        d.setTotalEstimado(total);
        devRepo.save(d);
        return ResponseEntity.ok(d);
    }
}

