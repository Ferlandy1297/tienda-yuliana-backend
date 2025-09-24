package com.tiendayuliana.backend.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VentaDetalleId implements Serializable {
    private Integer idVenta;
    private Integer idProducto;
    private Integer idLote; // Integer (no primitivo) para permitir null
    
}
