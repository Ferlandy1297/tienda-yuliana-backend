package com.example.demo.venta.dto;

import java.util.List;

public class VentaCreateRequest {
    private String tipo; // DETALLE, MAYOREO, FIADO
    private Integer idCliente; // opcional
    private List<VentaItemRequest> items;
    private VentaPagoRequest pago;

    // Getters explícitos para compatibilidad sin Lombok
    public String getTipo() { return tipo; }
    public Integer getIdCliente() { return idCliente; }
    public List<VentaItemRequest> getItems() { return items; }
    public VentaPagoRequest getPago() { return pago; }

    // Setters explícitos para compatibilidad sin Lombok
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public void setItems(List<VentaItemRequest> items) { this.items = items; }
    public void setPago(VentaPagoRequest pago) { this.pago = pago; }

}
