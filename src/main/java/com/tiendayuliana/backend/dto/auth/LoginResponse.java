package com.tiendayuliana.backend.dto.auth;

public record LoginResponse(
        Integer idUsuario,
        String nombreUsuario,
        String rol
) {}
