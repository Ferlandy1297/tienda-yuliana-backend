package com.tiendayuliana.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "cuenta_corriente", schema = "tienda_yuliana")
public class CuentaCorriente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Integer idCuenta;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_cliente", nullable = false, unique = true,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Cliente cliente;

    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(nullable = false, length = 15)
    private String estado = "AL_DIA"; // AL_DIA / EN_MORA / BLOQUEADO
}
