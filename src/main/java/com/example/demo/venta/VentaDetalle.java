package com.example.demo.venta;

import com.example.demo.lote.Lote;
import com.example.demo.producto.Producto;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "venta_detalle", schema = "tienda_yuliana")
public class VentaDetalle {
    @EmbeddedId
    private VentaDetalleId id;

    @ManyToOne(optional = false)
    @MapsId("idVenta")
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @ManyToOne(optional = false)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(optional = false)
    @MapsId("idLote")
    @JoinColumn(name = "id_lote", nullable = false)
    private Lote lote;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private BigDecimal descuento = BigDecimal.ZERO;

    // Getters/Setters explícitos para compatibilidad sin Lombok
    public VentaDetalleId getId() { return id; }
    public Venta getVenta() { return venta; }
    public Producto getProducto() { return producto; }
    public Lote getLote() { return lote; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getDescuento() { return descuento; }

    public void setId(VentaDetalleId id) { this.id = id; }
    public void setVenta(Venta venta) { this.venta = venta; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public void setLote(Lote lote) { this.lote = lote; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

}
