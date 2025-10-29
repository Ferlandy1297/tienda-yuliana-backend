package com.example.demo.venta.dto;

import java.math.BigDecimal;

public class VentaItemRequest {
    private Integer idProducto; // opcional si se pasa codigoBarras
    private String codigoBarras; // opcional si se pasa idProducto
    private Integer cantidad;
    private BigDecimal precioUnitario; // requerido para MAYOREO (precio por volumen)

    // Getters explícitos
    public Integer getIdProducto() { return idProducto; }
    public String getCodigoBarras() { return codigoBarras; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }

    // Setters explícitos
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

}
