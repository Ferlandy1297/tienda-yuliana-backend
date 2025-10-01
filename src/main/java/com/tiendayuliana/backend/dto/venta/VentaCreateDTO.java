package com.tiendayuliana.backend.dto.venta;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class VentaCreateDTO {

    // DETALLE / MAYOREO / FIADO
    @NotBlank
    private String tipo = "DETALLE";

    // cliente opcional; FIADO normalmente requiere cliente
    private Integer idCliente;

    @NotNull
    private Integer idUsuario;

    @NotEmpty
    @Valid
    private List<VentaDetalleCreateDTO> detalles;

    // pago opcional: si mandas pago, debe cubrir el total (calculamos cambio)
    @Valid
    private PagoCreateDTO pago;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public List<VentaDetalleCreateDTO> getDetalles() { return detalles; }
    public void setDetalles(List<VentaDetalleCreateDTO> detalles) { this.detalles = detalles; }
    public PagoCreateDTO getPago() { return pago; }
    public void setPago(PagoCreateDTO pago) { this.pago = pago; }
}
