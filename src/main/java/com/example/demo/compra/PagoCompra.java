package com.example.demo.compra;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pago_compra", schema = "tienda_yuliana")
public class PagoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago_compra")
    private Integer idPagoCompra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    @Column(nullable = false)
    private String metodo = "EFECTIVO"; // EFECTIVO

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(name = "fecha_hora", nullable = false)
    private OffsetDateTime fechaHora = OffsetDateTime.now();

    private String observacion;

    
    public Integer getIdPagoCompra() { return idPagoCompra; }
    public void setIdPagoCompra(Integer idPagoCompra) { this.idPagoCompra = idPagoCompra; }
    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    public java.math.BigDecimal getMonto() { return monto; }
    public void setMonto(java.math.BigDecimal monto) { this.monto = monto; }
    public java.time.OffsetDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(java.time.OffsetDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
