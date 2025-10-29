package com.example.demo.venta;

import com.example.demo.pago.Pago;
import com.example.demo.pago.PagoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class PagoController {
    private final VentaRepository ventaRepository;
    private final PagoRepository pagoRepository;

    public PagoController(VentaRepository ventaRepository, PagoRepository pagoRepository) {
        this.ventaRepository = ventaRepository;
        this.pagoRepository = pagoRepository;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping("/{id}/pago")
    public ResponseEntity<?> registrarPago(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        var venta = ventaRepository.findById(id).orElse(null);
        if (venta == null) return ResponseEntity.notFound().build();
        BigDecimal monto = new BigDecimal(body.get("monto").toString());
        if (monto.compareTo(BigDecimal.ZERO) < 0) return ResponseEntity.badRequest().build();
        BigDecimal total = venta.getTotal();
        BigDecimal yaPagado = pagoRepository.findAll().stream()
                .filter(p -> p.getVenta().getIdVenta().equals(id))
                .map(Pago::getMontoEntregado).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal restante = total.subtract(yaPagado);
        if (monto.compareTo(restante) < 0) {
            // pago parcial efectivo, no hay cambio
            Pago pago = new Pago();
            pago.setVenta(venta);
            pago.setMetodo("EFECTIVO");
            pago.setMontoEntregado(monto);
            pago.setCambioCalculado(BigDecimal.ZERO);
            pago.setFechaHora(OffsetDateTime.now());
            pagoRepository.save(pago);
            return ResponseEntity.ok(Map.of("restante", restante.subtract(monto)));
        } else {
            Pago pago = new Pago();
            pago.setVenta(venta);
            pago.setMetodo("EFECTIVO");
            pago.setMontoEntregado(monto);
            pago.setCambioCalculado(monto.subtract(restante));
            pago.setFechaHora(OffsetDateTime.now());
            pagoRepository.save(pago);
            return ResponseEntity.ok(new HashMap<>() {{
                put("restante", BigDecimal.ZERO);
                put("cambio", monto.subtract(restante));
            }});
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/{id}/comprobante")
    public ResponseEntity<?> comprobante(@PathVariable Integer id) {
        var venta = ventaRepository.findById(id).orElse(null);
        if (venta == null) return ResponseEntity.notFound().build();
        Map<String, Object> ticket = new HashMap<>();
        ticket.put("idVenta", venta.getIdVenta());
        ticket.put("fechaHora", venta.getFechaHora());
        ticket.put("cliente", venta.getCliente() != null ? venta.getCliente().getNombre() : null);
        ticket.put("usuario", venta.getUsuario().getNombreUsuario());
        ticket.put("tipo", venta.getTipo());
        ticket.put("total", venta.getTotal());
        // Para simplificar, los detalles y pagos se consultan via repos separadas por cliente si se necesita ampliar
        return ResponseEntity.ok(ticket);
    }
}

