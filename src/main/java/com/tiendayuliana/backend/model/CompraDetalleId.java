package com.tiendayuliana.backend.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Embeddable
public class CompraDetalleId implements Serializable {
    private Integer idCompra;
    private Integer idProducto;
    private Integer idLote;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompraDetalleId that = (CompraDetalleId) o;
        return Objects.equals(idCompra, that.idCompra) &&
               Objects.equals(idProducto, that.idProducto) &&
               Objects.equals(idLote, that.idLote);
    }
    @Override public int hashCode() { return Objects.hash(idCompra, idProducto, idLote); }
}
