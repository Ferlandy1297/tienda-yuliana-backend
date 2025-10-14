package com.tiendayuliana.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "pago", schema = "tienda_yuliana")
public class Pago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_venta", nullable = false, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Venta venta;

    @Column(nullable = false, length = 15)
    private String metodo = "EFECTIVO";

    @Column(name = "monto_entregado", nullable = false)
    private BigDecimal montoEntregado;

    @Column(name = "cambio_calculado", nullable = false)
    private BigDecimal cambioCalculado = BigDecimal.ZERO;

    @Column(name = "denominacion_billete")
    private BigDecimal denominacionBillete;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(length = 200)
    private String observacion;

    public Pago() {}

    // Getters and Setters
    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public BigDecimal getMontoEntregado() {
        return montoEntregado;
    }

    public void setMontoEntregado(BigDecimal montoEntregado) {
        this.montoEntregado = montoEntregado;
    }

    public BigDecimal getCambioCalculado() {
        return cambioCalculado;
    }

    public void setCambioCalculado(BigDecimal cambioCalculado) {
        this.cambioCalculado = cambioCalculado;
    }

    public BigDecimal getDenominacionBillete() {
        return denominacionBillete;
    }

    public void setDenominacionBillete(BigDecimal denominacionBillete) {
        this.denominacionBillete = denominacionBillete;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
