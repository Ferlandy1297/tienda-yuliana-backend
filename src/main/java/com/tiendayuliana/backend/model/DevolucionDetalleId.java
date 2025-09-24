package com.tiendayuliana.backend.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Embeddable
public class DevolucionDetalleId implements Serializable {
    private Integer idDevolucion;
    private Integer idProducto;
    private Integer idLote;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevolucionDetalleId that = (DevolucionDetalleId) o;
        return Objects.equals(idDevolucion, that.idDevolucion) &&
               Objects.equals(idProducto, that.idProducto) &&
               Objects.equals(idLote, that.idLote);
    }
    @Override public int hashCode() { return Objects.hash(idDevolucion, idProducto, idLote); }
}
