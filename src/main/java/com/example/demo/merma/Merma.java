package com.example.demo.merma;

import com.example.demo.lote.Lote;
import com.example.demo.producto.Producto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "merma", schema = "tienda_yuliana")
public class Merma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_merma")
    private Integer idMerma;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_lote")
    private Lote lote;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private String motivo; // DAÑO, VENCIMIENTO, OTRO

    @Column(nullable = false, name = "fecha")
    private OffsetDateTime fecha = OffsetDateTime.now();

    @Column(name = "costo_estimado", nullable = false)
    private BigDecimal costoEstimado = BigDecimal.ZERO;

    private String observacion;

    // Setters explícitos para compatibilidad sin Lombok
    public void setProducto(Producto producto) { this.producto = producto; }
    public void setLote(Lote lote) { this.lote = lote; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public void setCostoEstimado(BigDecimal costoEstimado) { this.costoEstimado = costoEstimado; }

    // Getters mínimos si se necesitan en respuestas
    public Integer getIdMerma() { return idMerma; }
    public Producto getProducto() { return producto; }
    public Lote getLote() { return lote; }
    public Integer getCantidad() { return cantidad; }
    public String getMotivo() { return motivo; }
    public OffsetDateTime getFecha() { return fecha; }
    public BigDecimal getCostoEstimado() { return costoEstimado; }
    public String getObservacion() { return observacion; }

}
