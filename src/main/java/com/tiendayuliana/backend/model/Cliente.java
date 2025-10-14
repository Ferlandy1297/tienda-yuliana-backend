package com.tiendayuliana.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "cliente", schema = "tienda_yuliana")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    public Integer getIdCliente() {
        return idCliente;
    }

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 30)
    private String telefono;

    @Column(length = 30)
    private String nit;

    @Column(name = "es_mayorista", nullable = false)
    private boolean esMayorista = false;

    @Column(name = "limite_credito", nullable = false)
    private BigDecimal limiteCredito = BigDecimal.ZERO;

    @Column(name = "estado_credito", nullable = false, length = 15)
    private String estadoCredito = "ACTIVO";

    @Column(nullable = false)
    private Boolean activo = true;

    // Getter for esMayorista
    public boolean isEsMayorista() {
        return esMayorista;
    }

    // Setter for esMayorista
    public void setEsMayorista(boolean esMayorista) {
        this.esMayorista = esMayorista;
    }
}
