package com.example.demo.devolucion;

import com.example.demo.lote.Lote;
import com.example.demo.producto.Producto;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "devolucion_detalle", schema = "tienda_yuliana")
public class DevolucionDetalle {
    @EmbeddedId
    private DevolucionDetalleId id;

    @ManyToOne(optional = false)
    @MapsId("idDevolucion")
    @JoinColumn(name = "id_devolucion", nullable = false)
    private DevolucionProveedor devolucion;

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

    @Column(name = "costo_estimado", nullable = false)
    private BigDecimal costoEstimado = BigDecimal.ZERO;

    
    
    public void setId(DevolucionDetalleId id) { this.id = id; }
    public DevolucionProveedor getDevolucion() { return devolucion; }
    public void setDevolucion(DevolucionProveedor devolucion) { this.devolucion = devolucion; }
    public com.example.demo.producto.Producto getProducto() { return producto; }
    public void setProducto(com.example.demo.producto.Producto producto) { this.producto = producto; }
    public com.example.demo.lote.Lote getLote() { return lote; }
    public void setLote(com.example.demo.lote.Lote lote) { this.lote = lote; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getCostoEstimado() { return costoEstimado; }
    public void setCostoEstimado(BigDecimal costoEstimado) { this.costoEstimado = costoEstimado; }
}
