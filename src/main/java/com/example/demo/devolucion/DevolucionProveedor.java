package com.example.demo.devolucion;

import com.example.demo.producto.Proveedor;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "devolucion_proveedor", schema = "tienda_yuliana")
public class DevolucionProveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucion")
    private Integer idDevolucion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @Column(name = "fecha", nullable = false)
    private OffsetDateTime fecha = OffsetDateTime.now();

    private String motivo;

    @Column(name = "total_estimado", nullable = false)
    private BigDecimal totalEstimado = BigDecimal.ZERO;

    // Setters/Getters explícitos para compatibilidad con IDEs sin AP
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    public void setFecha(OffsetDateTime fecha) { this.fecha = fecha; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public void setTotalEstimado(BigDecimal totalEstimado) { this.totalEstimado = totalEstimado; }

    public Integer getIdDevolucion() { return idDevolucion; }
    public Proveedor getProveedor() { return proveedor; }
    public OffsetDateTime getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
    public BigDecimal getTotalEstimado() { return totalEstimado; }

}
