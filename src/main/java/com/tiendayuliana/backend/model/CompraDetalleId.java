package com.tiendayuliana.backend.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Embeddable
public class CompraDetalleId implements Serializable {
    private Integer idCompra;
    private Integer idProducto;
    private Integer idLote;

    // Setters (in case Lombok not working)
    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
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
        CompraDetalleId that = (CompraDetalleId) o;
        return Objects.equals(idCompra, that.idCompra)
                && Objects.equals(idProducto, that.idProducto)
                && Objects.equals(idLote, that.idLote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra, idProducto, idLote);
    }
}
