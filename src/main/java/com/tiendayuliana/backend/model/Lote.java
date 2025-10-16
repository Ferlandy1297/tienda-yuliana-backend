package com.tiendayuliana.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity @Table(name = "lote", schema = "tienda_yuliana")
public class Lote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lote")
    private Integer idLote;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Producto producto;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible = 0;

    @Column(name = "en_descuento")
    private Boolean enDescuento = Boolean.FALSE;

    @Column(name = "porcentaje_descuento")
    private BigDecimal porcentajeDescuento; // null o 0 si no aplica

    public Lote(Integer idLote, Producto producto, LocalDate fechaVencimiento, Integer cantidadDisponible) {
        this.idLote = idLote;
        this.producto = producto;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadDisponible = cantidadDisponible;
    }

    // Getters and Setters
    public Integer getIdLote() { return idLote; }
    public void setIdLote(Integer idLote) { this.idLote = idLote; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public Integer getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(Integer cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
    public Boolean getEnDescuento() { return enDescuento; }
    public void setEnDescuento(Boolean enDescuento) { this.enDescuento = enDescuento; }
    public BigDecimal getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }
}
