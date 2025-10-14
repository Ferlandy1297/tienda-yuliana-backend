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
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity @Table(name = "venta", schema = "tienda_yuliana")
public class Venta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(nullable = false, length = 15)
    private String tipo = "DETALLE";

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "id_cliente", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private UsuarioSis usuario;

    // Getters and Setters
    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public UsuarioSis getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioSis usuario) {
        this.usuario = usuario;
    }
}
