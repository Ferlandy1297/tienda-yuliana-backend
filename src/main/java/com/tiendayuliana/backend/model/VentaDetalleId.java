package com.tiendayuliana.backend.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
}
