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
@Entity @Table(name = "pago_compra", schema = "tienda_yuliana")
public class PagoCompra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago_compra")
    private Integer idPagoCompra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_compra", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Compra compra;

    @Column(nullable = false, length = 15)
    private String metodo = "EFECTIVO"; // (tu DDL restringe a EFECTIVO)

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(length = 200)
    private String observacion;

    public PagoCompra() {}

    // Getters and Setters
    public Integer getIdPagoCompra() {
        return idPagoCompra;
    }

    public void setIdPagoCompra(Integer idPagoCompra) {
        this.idPagoCompra = idPagoCompra;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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
