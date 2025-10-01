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
@Entity @Table(name = "merma", schema = "tienda_yuliana")
public class Merma {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_merma")
    private Integer idMerma;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_lote",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Lote lote;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 15)
    private String motivo; // DAÑO / VENCIMIENTO / OTRO MOTIVO

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name = "costo_estimado", nullable = false)
    private BigDecimal costoEstimado = BigDecimal.ZERO;

    @Column(length = 200)
    private String observacion;
}
