package com.example.demo.compra.dto;

 
import java.math.BigDecimal;
import java.time.LocalDate;

public class CompraItemRequest {
    private Integer idProducto;
    private Integer cantidad;
    private BigDecimal costoUnitario;
    private LocalDate fechaVencimiento; // opcional

    // Métodos explícitos para compatibilidad con IDEs sin AP
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(BigDecimal costoUnitario) { this.costoUnitario = costoUnitario; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

}
