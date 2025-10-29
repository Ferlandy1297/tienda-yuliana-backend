package com.example.demo.venta;

import com.example.demo.cliente.Cliente;
import com.example.demo.user.UsuarioSis;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "venta", schema = "tienda_yuliana")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @Column(name = "fecha_hora", nullable = false)
    private OffsetDateTime fechaHora = OffsetDateTime.now();

    @Column(name = "tipo", nullable = false)
    private String tipo = "DETALLE"; // DETALLE, MAYOREO, FIADO

    @Column(name = "total", nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioSis usuario;

    // Getters/Setters explícitos para compatibilidad sin Lombok
    public Integer getIdVenta() { return idVenta; }
    public OffsetDateTime getFechaHora() { return fechaHora; }
    public String getTipo() { return tipo; }
    public BigDecimal getTotal() { return total; }
    public Cliente getCliente() { return cliente; }
    public UsuarioSis getUsuario() { return usuario; }

    public void setFechaHora(OffsetDateTime fechaHora) { this.fechaHora = fechaHora; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setUsuario(UsuarioSis usuario) { this.usuario = usuario; }

}
