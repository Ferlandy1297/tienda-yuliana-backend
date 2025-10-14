package com.tiendayuliana.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
@Entity @Table(name = "compra_detalle", schema = "tienda_yuliana")
public class CompraDetalle {
    @EmbeddedId
    private CompraDetalleId id;

    @MapsId("idCompra")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_compra", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Compra compra;

    @MapsId("idProducto")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Producto producto;

    @MapsId("idLote")
    @ManyToOne
    @JoinColumn(name = "id_lote",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Lote lote;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "costo_unitario", nullable = false)
    private BigDecimal costoUnitario;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    public CompraDetalle() {}

    // Getters and Setters
    public CompraDetalleId getId() {
        return id;
    }

    public void setId(CompraDetalleId id) {
        this.id = id;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
