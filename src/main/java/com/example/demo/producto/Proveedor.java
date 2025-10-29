package com.example.demo.producto;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedor", schema = "tienda_yuliana")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(nullable = false, length = 120)
    private String nombre;

    private String contacto;
    private String telefono;
    private String direccion;

    @Column(nullable = false)
    private Boolean activo = true;

    // Getters/Setters explícitos para compatibilidad sin Lombok
    public Integer getIdProveedor() { return idProveedor; }
    public String getNombre() { return nombre; }
    public String getContacto() { return contacto; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public Boolean getActivo() { return activo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setActivo(Boolean activo) { this.activo = activo; }

}
