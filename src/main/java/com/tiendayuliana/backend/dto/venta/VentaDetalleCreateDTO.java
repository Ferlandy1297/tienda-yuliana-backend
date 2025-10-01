package com.tiendayuliana.backend.dto.venta;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class VentaDetalleCreateDTO {

    @NotNull
    private Integer idProducto;

    // opcional: si vendes por lote específico
    private Integer idLote;

    @NotNull @Min(1)
    private Integer cantidad;

    @NotNull @DecimalMin(value = "0.01")
    private BigDecimal precioUnitario;

    @DecimalMin(value = "0.00")
    private BigDecimal descuento = BigDecimal.ZERO;

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getIdLote() { return idLote; }
    public void setIdLote(Integer idLote) { this.idLote = idLote; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
}
