package com.tiendayuliana.backend.model;

import java.math.BigDecimal;

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
@Entity @Table(name = "producto", schema = "tienda_yuliana")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(nullable = false, length = 160)
    private String nombre;

    @Column(name = "codigo_barras", length = 50, unique = true)
    private String codigoBarras;

    @Column(name = "precio_venta", nullable = false)
    private BigDecimal precioVenta;

    @Column(name = "costo_actual", nullable = false)
    private BigDecimal costoActual = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 0;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Proveedor proveedor;

    @Column(nullable = false)
    private Boolean activo = true;

    public Producto() {}

    // Getters and Setters
    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public BigDecimal getCostoActual() {
        return costoActual;
    }

    public void setCostoActual(BigDecimal costoActual) {
        this.costoActual = costoActual;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
