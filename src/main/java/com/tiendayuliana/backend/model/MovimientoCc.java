package com.tiendayuliana.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "movimiento_cc", schema = "tienda_yuliana")
public class MovimientoCc {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cuenta", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private CuentaCorriente cuenta;

    @Column(nullable = false, length = 10)
    private String tipo; // CARGO / ABONO

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false, name = "fecha")
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(length = 200)
    private String referencia;
}
