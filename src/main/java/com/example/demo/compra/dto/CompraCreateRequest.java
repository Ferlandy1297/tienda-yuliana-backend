package com.example.demo.compra.dto;

 
import java.util.List;

public class CompraCreateRequest {
    private Integer idProveedor;
    private String condicion; // CONTADO o CREDITO
    private String estado; // ABIERTA, PAGADA, PARCIAL
    private String observacion;
    private List<CompraItemRequest> items;

    // Métodos explícitos para compatibilidad con IDEs sin AP
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
    public String getCondicion() { return condicion; }
    public void setCondicion(String condicion) { this.condicion = condicion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public List<CompraItemRequest> getItems() { return items; }
    public void setItems(List<CompraItemRequest> items) { this.items = items; }

}
