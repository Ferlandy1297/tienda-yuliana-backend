package com.example.demo.cliente.dto;

 
import java.math.BigDecimal;

public class ClienteRequest {
    private String nombre;
    private String telefono;
    private String nit;
    private Boolean esMayorista;
    private BigDecimal limiteCredito;
    private String estadoCredito; // ACTIVO, BLOQUEADO
    private Boolean activo;

    // Métodos explícitos para IDEs sin Lombok (override de @Data)
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public Boolean getEsMayorista() { return esMayorista; }
    public void setEsMayorista(Boolean esMayorista) { this.esMayorista = esMayorista; }
    public BigDecimal getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(BigDecimal limiteCredito) { this.limiteCredito = limiteCredito; }
    public String getEstadoCredito() { return estadoCredito; }
    public void setEstadoCredito(String estadoCredito) { this.estadoCredito = estadoCredito; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
