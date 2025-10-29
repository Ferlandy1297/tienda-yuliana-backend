package com.example.demo.fiado;

import com.example.demo.cliente.Cliente;
import com.example.demo.cliente.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/fiados")
public class FiadoController {
    private final CuentaCorrienteRepository cuentaRepo;
    private final MovimientoCcRepository movRepo;
    private final ClienteRepository clienteRepo;

    public FiadoController(CuentaCorrienteRepository cuentaRepo, MovimientoCcRepository movRepo, ClienteRepository clienteRepo) {
        this.cuentaRepo = cuentaRepo;
        this.movRepo = movRepo;
        this.clienteRepo = clienteRepo;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/saldo/{idCliente}")
    public ResponseEntity<Map<String, Object>> saldo(@PathVariable Integer idCliente) {
        Cliente c = clienteRepo.findById(idCliente).orElse(null);
        if (c == null) return ResponseEntity.notFound().build();
        CuentaCorriente cc = cuentaRepo.findByCliente(c).orElse(null);
        BigDecimal saldo = cc != null ? cc.getSaldo() : BigDecimal.ZERO;
        String estado = cc != null ? cc.getEstado() : "AL_DIA";
        return ResponseEntity.ok(Map.of("idCliente", idCliente, "saldo", saldo, "estado", estado));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping("/abonos")
    public ResponseEntity<Map<String, Object>> abonar(@RequestBody Map<String, Object> body) {
        Integer idCliente = (Integer) body.get("idCliente");
        Object montoObj = body.get("monto");
        if (idCliente == null || montoObj == null) return ResponseEntity.badRequest().build();
        BigDecimal monto = new BigDecimal(montoObj.toString());
        if (monto.compareTo(BigDecimal.ZERO) < 0) return ResponseEntity.badRequest().build();

        Cliente c = clienteRepo.findById(idCliente).orElse(null);
        if (c == null) return ResponseEntity.notFound().build();
        CuentaCorriente cc = cuentaRepo.findByCliente(c).orElseGet(() -> {
            CuentaCorriente n = new CuentaCorriente();
            n.setCliente(c);
            n.setSaldo(BigDecimal.ZERO);
            n.setEstado("AL_DIA");
            return cuentaRepo.save(n);
        });

        cc.setSaldo(cc.getSaldo().subtract(monto));
        if (cc.getSaldo().compareTo(BigDecimal.ZERO) <= 0) {
            cc.setSaldo(BigDecimal.ZERO);
            cc.setEstado("AL_DIA");
        } else {
            cc.setEstado("EN_MORA");
        }
        cuentaRepo.save(cc);

        MovimientoCc mov = new MovimientoCc();
        mov.setCuenta(cc);
        mov.setTipo("ABONO");
        mov.setMonto(monto);
        mov.setReferencia("Abono manual");
        movRepo.save(mov);

        return ResponseEntity.ok(Map.of("saldo", cc.getSaldo(), "estado", cc.getEstado()));
    }
}

