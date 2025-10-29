package com.example.demo.compra.dto;

 
import java.math.BigDecimal;

public class PagoCompraRequest {
    private Integer idCompra;
    private String metodo; // EFECTIVO
    private BigDecimal monto;
    private String observacion;

    // Métodos explícitos para compatibilidad con IDEs sin AP
    public Integer getIdCompra() { return idCompra; }
    public void setIdCompra(Integer idCompra) { this.idCompra = idCompra; }
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

}
