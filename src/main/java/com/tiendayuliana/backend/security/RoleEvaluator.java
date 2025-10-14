package com.tiendayuliana.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component("roles")
public class RoleEvaluator {
    private final HttpServletRequest request;
    public RoleEvaluator(HttpServletRequest request) { this.request = request; }

    public boolean has(String role) {
        // Para entorno actual: se permite simular rol vía cabecera X-Role.
        // Si no viene, se asume ADMIN para no bloquear endpoints.
        String header = request.getHeader("X-Role");
        String current = header == null ? "ADMIN" : header.trim().toUpperCase();
        return current.equals(role.toUpperCase());
    }
}

