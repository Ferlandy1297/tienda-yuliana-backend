package com.example.demo.lote;

import com.example.demo.producto.Producto;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "lote", schema = "tienda_yuliana")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lote")
    private Integer idLote;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible = 0;

    // Setter explícito para compatibilidad con IDEs sin AP
    public void setProducto(Producto producto) { this.producto = producto; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public void setCantidadDisponible(Integer cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    // Getter explícito para compatibilidad con IDEs sin AP
    public Integer getIdLote() { return idLote; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public Integer getCantidadDisponible() { return cantidadDisponible; }

}
