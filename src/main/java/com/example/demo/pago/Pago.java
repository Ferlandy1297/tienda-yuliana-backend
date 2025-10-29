package com.example.demo.pago;

import com.example.demo.venta.Venta;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pago", schema = "tienda_yuliana")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @Column(name = "metodo", nullable = false)
    private String metodo = "EFECTIVO";

    @Column(name = "monto_entregado", nullable = false)
    private BigDecimal montoEntregado;

    @Column(name = "cambio_calculado", nullable = false)
    private BigDecimal cambioCalculado = BigDecimal.ZERO;

    @Column(name = "denominacion_billete")
    private BigDecimal denominacionBillete;

    @Column(name = "fecha_hora", nullable = false)
    private OffsetDateTime fechaHora = OffsetDateTime.now();

    private String observacion;

    
    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }
    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    public java.math.BigDecimal getMontoEntregado() { return montoEntregado; }
    public void setMontoEntregado(java.math.BigDecimal montoEntregado) { this.montoEntregado = montoEntregado; }
    public java.math.BigDecimal getCambioCalculado() { return cambioCalculado; }
    public void setCambioCalculado(java.math.BigDecimal cambioCalculado) { this.cambioCalculado = cambioCalculado; }
    public java.math.BigDecimal getDenominacionBillete() { return denominacionBillete; }
    public void setDenominacionBillete(java.math.BigDecimal denominacionBillete) { this.denominacionBillete = denominacionBillete; }
    public java.time.OffsetDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(java.time.OffsetDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
