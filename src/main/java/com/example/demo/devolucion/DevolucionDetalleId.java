package com.example.demo.devolucion;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class DevolucionDetalleId implements Serializable {
    @Column(name = "id_devolucion")
    private Integer idDevolucion;
    @Column(name = "id_producto")
    private Integer idProducto;
    @Column(name = "id_lote")
    private Integer idLote;

    public DevolucionDetalleId(Integer idDevolucion, Integer idProducto, Integer idLote) {
        this.idDevolucion = idDevolucion;
        this.idProducto = idProducto;
        this.idLote = idLote;
    }

    public DevolucionDetalleId() {}

    public Integer getIdDevolucion() { return idDevolucion; }
    public void setIdDevolucion(Integer idDevolucion) { this.idDevolucion = idDevolucion; }
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getIdLote() { return idLote; }
    public void setIdLote(Integer idLote) { this.idLote = idLote; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevolucionDetalleId that = (DevolucionDetalleId) o;
        return java.util.Objects.equals(idDevolucion, that.idDevolucion)
                && java.util.Objects.equals(idProducto, that.idProducto)
                && java.util.Objects.equals(idLote, that.idLote);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idDevolucion, idProducto, idLote);
    }
}
