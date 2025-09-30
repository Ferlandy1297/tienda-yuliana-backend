package com.tiendayuliana.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductUpdateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 160)
    private String nombre;

    @Size(max = 50)
    private String codigoBarras; // puede ser null o vacío

    @NotNull @DecimalMin(value = "0.01")
    private BigDecimal precioVenta;

    @NotNull @DecimalMin(value = "0.00")
    private BigDecimal costoActual;

    @NotNull @Min(0)
    private Integer stock;

    @NotNull @Min(0)
    private Integer stockMinimo;

    private Integer idProveedor; // opcional

    private Boolean activo; // para activar/desactivar

    // getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public BigDecimal getCostoActual() { return costoActual; }
    public void setCostoActual(BigDecimal costoActual) { this.costoActual = costoActual; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
