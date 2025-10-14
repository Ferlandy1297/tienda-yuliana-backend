package com.tiendayuliana.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity @Table(name = "promocion", schema = "tienda_yuliana")
public class Promocion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion")
    private Integer idPromocion;
    @Column(nullable = false, length = 160)
    private String nombre;
    @Column(nullable = false)
    private BigDecimal porcentaje; // 0.00 - 1.00
    private LocalDate desde;
    private LocalDate hasta;
    @Column(nullable = false)
    private Boolean activo = true;

    public Integer getIdPromocion() { return idPromocion; }
    public void setIdPromocion(Integer idPromocion) { this.idPromocion = idPromocion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
    public LocalDate getDesde() { return desde; }
    public void setDesde(LocalDate desde) { this.desde = desde; }
    public LocalDate getHasta() { return hasta; }
    public void setHasta(LocalDate hasta) { this.hasta = hasta; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}

