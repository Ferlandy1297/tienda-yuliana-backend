package com.example.demo.venta;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class VentaDetalleId implements Serializable {
    @Column(name = "id_venta")
    private Integer idVenta;
    @Column(name = "id_producto")
    private Integer idProducto;
    @Column(name = "id_lote")
    private Integer idLote;

    public VentaDetalleId(Integer idVenta, Integer idProducto, Integer idLote) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.idLote = idLote;
    }

    public VentaDetalleId() {}

    public Integer getIdVenta() { return idVenta; }
    public void setIdVenta(Integer idVenta) { this.idVenta = idVenta; }
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getIdLote() { return idLote; }
    public void setIdLote(Integer idLote) { this.idLote = idLote; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VentaDetalleId that = (VentaDetalleId) o;
        return java.util.Objects.equals(idVenta, that.idVenta)
                && java.util.Objects.equals(idProducto, that.idProducto)
                && java.util.Objects.equals(idLote, that.idLote);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idVenta, idProducto, idLote);
    }
}
