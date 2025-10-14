package com.tiendayuliana.backend.dto.proveedor;

public record ProveedorResponseDTO(
        Integer idProveedor,
        String nombre,
        String contacto,
        String telefono,
        Boolean activo
) {}

