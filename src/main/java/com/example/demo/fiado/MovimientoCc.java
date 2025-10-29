package com.example.demo.fiado;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "movimiento_cc", schema = "tienda_yuliana")
public class MovimientoCc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private CuentaCorriente cuenta;

    @Column(nullable = false)
    private String tipo; // CARGO, ABONO

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false, name = "fecha")
    private OffsetDateTime fecha = OffsetDateTime.now();

    private String referencia;

    // Setter explícito para compatibilidad con IDEs sin AP
    public void setCuenta(CuentaCorriente cuenta) { this.cuenta = cuenta; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setMonto(java.math.BigDecimal monto) { this.monto = monto; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

}
