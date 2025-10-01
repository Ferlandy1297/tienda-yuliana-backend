package com.tiendayuliana.backend.dto.venta;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class PagoCreateDTO {

    // tu DDL solo permite EFECTIVO; dejamos el campo por compatibilidad futura
    private String metodo = "EFECTIVO";

    @NotNull @DecimalMin(value = "0.00")
    private BigDecimal montoEntregado;

    // opcional (para caja)
    private BigDecimal denominacionBillete;

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    public BigDecimal getMontoEntregado() { return montoEntregado; }
    public void setMontoEntregado(BigDecimal montoEntregado) { this.montoEntregado = montoEntregado; }
    public BigDecimal getDenominacionBillete() { return denominacionBillete; }
    public void setDenominacionBillete(BigDecimal denominacionBillete) { this.denominacionBillete = denominacionBillete; }
}
