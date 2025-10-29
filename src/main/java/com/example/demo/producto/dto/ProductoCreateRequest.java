package com.example.demo.producto.dto;

import java.math.BigDecimal;

public class ProductoCreateRequest {
    private String nombre;
    private String codigoBarras;
    private BigDecimal precioVenta;
    private BigDecimal costoActual;
    private Integer stock;
    private Integer stockMinimo;
    private Integer idProveedor; // opcional

    // Getters explícitos para compatibilidad sin Lombok
    public String getNombre() { return nombre; }
    public String getCodigoBarras() { return codigoBarras; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public BigDecimal getCostoActual() { return costoActual; }
    public Integer getStock() { return stock; }
    public Integer getStockMinimo() { return stockMinimo; }
    public Integer getIdProveedor() { return idProveedor; }

    // Setters explícitos para compatibilidad sin Lombok
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public void setCostoActual(BigDecimal costoActual) { this.costoActual = costoActual; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

}
