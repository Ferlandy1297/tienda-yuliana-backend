package com.tiendayuliana.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "devolucion_detalle", schema = "tienda_yuliana")
public class DevolucionDetalle {
    @EmbeddedId
    private DevolucionDetalleId id;

    @MapsId("idDevolucion")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_devolucion", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private DevolucionProveedor devolucion;

    @MapsId("idProducto")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Producto producto;

    @MapsId("idLote")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_lote", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Lote lote;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "costo_estimado", nullable = false)
    private BigDecimal costoEstimado = BigDecimal.ZERO;
}
