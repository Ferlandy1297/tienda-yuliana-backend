package com.tiendayuliana.backend.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Embeddable
public class VentaDetalleId implements Serializable {
    private Integer idVenta;
    private Integer idProducto;
    private Integer idLote; // Integer (no primitivo) para permitir null

    public VentaDetalleId() {}

    public VentaDetalleId(Integer idVenta, Integer idProducto, Integer idLote) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.idLote = idLote;
    }

    // Setters (in case Lombok not working)
    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VentaDetalleId that = (VentaDetalleId) o;
        return Objects.equals(idVenta, that.idVenta)
                && Objects.equals(idProducto, that.idProducto)
                && Objects.equals(idLote, that.idLote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenta, idProducto, idLote);
    }
}
