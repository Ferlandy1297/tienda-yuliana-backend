package com.tiendayuliana.backend.dto.caducidad;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CaducidadAccionDTO {
    @NotNull
    private String accion; // DESCUENTO | DONACION | DEVOLUCION

    @NotEmpty @Valid
    private List<Item> items;

    // Opcional; aplica para DESCUENTO (0 < p <= 1)
    private BigDecimal porcentajeDescuento;

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public BigDecimal getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    public static class Item {
        @NotNull private Integer idLote;
        @NotNull private Integer cantidad;

        public Integer getIdLote() { return idLote; }
        public void setIdLote(Integer idLote) { this.idLote = idLote; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}

