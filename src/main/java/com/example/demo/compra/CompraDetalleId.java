package com.example.demo.compra;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CompraDetalleId implements Serializable {
    @Column(name = "id_compra")
    private Integer idCompra;
    @Column(name = "id_producto")
    private Integer idProducto;
    @Column(name = "id_lote")
    private Integer idLote;

    public CompraDetalleId(Integer idCompra, Integer idProducto, Integer idLote) {
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.idLote = idLote;
    }

    public CompraDetalleId() {}

    public Integer getIdCompra() { return idCompra; }
    public void setIdCompra(Integer idCompra) { this.idCompra = idCompra; }
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getIdLote() { return idLote; }
    public void setIdLote(Integer idLote) { this.idLote = idLote; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompraDetalleId that = (CompraDetalleId) o;
        return java.util.Objects.equals(idCompra, that.idCompra)
                && java.util.Objects.equals(idProducto, that.idProducto)
                && java.util.Objects.equals(idLote, that.idLote);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idCompra, idProducto, idLote);
    }
}
