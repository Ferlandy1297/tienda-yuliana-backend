package com.example.demo.producto.dto;


public class ProveedorRequest {
    private String nombre;
    private String contacto;
    private String telefono;
    private String direccion;
    private Boolean activo;

    // Getters explícitos para compatibilidad sin Lombok
    public String getNombre() { return nombre; }
    public String getContacto() { return contacto; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public Boolean getActivo() { return activo; }

    // Setters explícitos para compatibilidad sin Lombok
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setActivo(Boolean activo) { this.activo = activo; }

}
