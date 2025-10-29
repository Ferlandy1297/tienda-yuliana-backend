package com.example.demo.cliente;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente", schema = "tienda_yuliana")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(nullable = false, length = 120)
    private String nombre;

    private String telefono;
    private String nit;

    @Column(name = "es_mayorista", nullable = false)
    private Boolean esMayorista = false;

    @Column(name = "limite_credito", nullable = false)
    private java.math.BigDecimal limiteCredito = java.math.BigDecimal.ZERO;

    @Column(name = "estado_credito", nullable = false)
    private String estadoCredito = "ACTIVO";

    @Column(nullable = false)
    private Boolean activo = true;

    // Setters explícitos (además de Lombok) para compatibilidad con IDEs sin AP
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setNit(String nit) { this.nit = nit; }
    public void setEsMayorista(Boolean esMayorista) { this.esMayorista = esMayorista; }
    public void setLimiteCredito(java.math.BigDecimal limiteCredito) { this.limiteCredito = limiteCredito; }
    public void setEstadoCredito(String estadoCredito) { this.estadoCredito = estadoCredito; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    // Getters explícitos para compatibilidad sin Lombok
    public Integer getIdCliente() { return idCliente; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getNit() { return nit; }
    public Boolean getEsMayorista() { return esMayorista; }
    public java.math.BigDecimal getLimiteCredito() { return limiteCredito; }
    public String getEstadoCredito() { return estadoCredito; }
    public Boolean getActivo() { return activo; }

}
