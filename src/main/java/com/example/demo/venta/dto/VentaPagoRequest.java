package com.example.demo.venta.dto;

import java.math.BigDecimal;

public class VentaPagoRequest {
    private BigDecimal montoEntregado; // solo efectivo
    private BigDecimal denominacionBillete; // opcional

    // Getters explícitos
    public BigDecimal getMontoEntregado() { return montoEntregado; }
    public BigDecimal getDenominacionBillete() { return denominacionBillete; }

    // Setters explícitos
    public void setMontoEntregado(BigDecimal montoEntregado) { this.montoEntregado = montoEntregado; }
    public void setDenominacionBillete(BigDecimal denominacionBillete) { this.denominacionBillete = denominacionBillete; }

}
