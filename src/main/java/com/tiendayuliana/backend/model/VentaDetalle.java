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
@Entity @Table(name = "venta_detalle", schema = "tienda_yuliana")
public class VentaDetalle {
    @EmbeddedId
    private VentaDetalleId id;

    @MapsId("idVenta")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_venta", nullable = false, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Venta venta;

    @MapsId("idProducto")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Producto producto;

    @MapsId("idLote")
    @ManyToOne
    @JoinColumn(name = "id_lote", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Lote lote;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private BigDecimal descuento = BigDecimal.ZERO;
}
