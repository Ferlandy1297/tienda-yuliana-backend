package com.example.demo.compra;

import com.example.demo.lote.Lote;
import com.example.demo.producto.Producto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "compra_detalle", schema = "tienda_yuliana")
public class CompraDetalle {
    @EmbeddedId
    private CompraDetalleId id;

    @ManyToOne(optional = false)
    @MapsId("idCompra")
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    @ManyToOne(optional = false)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @MapsId("idLote")
    @JoinColumn(name = "id_lote")
    private Lote lote;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "costo_unitario", nullable = false)
    private BigDecimal costoUnitario;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    
    public CompraDetalleId getId() { return id; }
    public void setId(CompraDetalleId id) { this.id = id; }
    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }
    public com.example.demo.producto.Producto getProducto() { return producto; }
    public void setProducto(com.example.demo.producto.Producto producto) { this.producto = producto; }
    public com.example.demo.lote.Lote getLote() { return lote; }
    public void setLote(com.example.demo.lote.Lote lote) { this.lote = lote; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public java.math.BigDecimal getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(java.math.BigDecimal costoUnitario) { this.costoUnitario = costoUnitario; }
    public java.time.LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(java.time.LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
}
