package com.example.demo.producto;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "producto", schema = "tienda_yuliana")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(nullable = false, length = 160)
    private String nombre;

    @Column(name = "codigo_barras", unique = true)
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
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @Column(nullable = false)
    private Boolean activo = true;

    // Getter explícito para compatibilidad con IDEs sin AP
    public Integer getIdProducto() { return idProducto; }
    public Integer getStock() { return stock; }
    public BigDecimal getCostoActual() { return costoActual; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public Integer getStockMinimo() { return stockMinimo; }
    public Boolean getActivo() { return activo; }
    public String getNombre() { return nombre; }

    // Setter explícito solicitado por el IDE
    public void setCostoActual(BigDecimal costoActual) { this.costoActual = costoActual; }
    public void setStock(int stock) { this.stock = stock; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

}
