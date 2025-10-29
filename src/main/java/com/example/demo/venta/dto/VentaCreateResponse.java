package com.example.demo.venta.dto;


public class VentaCreateResponse {
    private Integer idVenta;
    private java.math.BigDecimal total;
    private java.math.BigDecimal cambio;

    // Getters/Setters explícitos para compatibilidad sin Lombok
    public Integer getIdVenta() { return idVenta; }
    public java.math.BigDecimal getTotal() { return total; }
    public java.math.BigDecimal getCambio() { return cambio; }

    public void setIdVenta(Integer idVenta) { this.idVenta = idVenta; }
    public void setTotal(java.math.BigDecimal total) { this.total = total; }
    public void setCambio(java.math.BigDecimal cambio) { this.cambio = cambio; }

}
