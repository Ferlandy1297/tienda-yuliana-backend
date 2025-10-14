package com.tiendayuliana.backend.dto.proveedor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProveedorUpdateDTO(
        @NotBlank @Size(max = 160) String nombre,
        @Size(max = 120) String contacto,
        @Size(max = 60) String telefono,
        Boolean activo
) {}

