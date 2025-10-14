package com.tiendayuliana.backend.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.auth.LoginRequest;
import com.tiendayuliana.backend.dto.auth.LoginResponse;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.model.UsuarioSis;
import com.tiendayuliana.backend.repository.UsuarioSisRepository;
import com.tiendayuliana.backend.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioSisRepository usuarioSisRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthServiceImpl(UsuarioSisRepository usuarioSisRepository) {
        this.usuarioSisRepository = usuarioSisRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String username = request.nombreUsuario() == null ? "" : request.nombreUsuario().trim();
        if (username.isEmpty()) {
            throw new BadRequestException("El nombre de usuario es obligatorio");
        }
        UsuarioSis usuario = usuarioSisRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new BadRequestException("Credenciales inválidas"));

        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new BadRequestException("El usuario está inactivo");
        }

        String rawPassword = request.password();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new BadRequestException("La contraseña es obligatoria");
        }

        if (!matches(usuario.getPasswordHash(), rawPassword)) {
            throw new BadRequestException("Credenciales inválidas");
        }

        return new LoginResponse(usuario.getIdUsuario(), usuario.getNombreUsuario(), usuario.getRol());
    }

    private boolean matches(String storedHash, String rawPassword) {
        if (storedHash == null) {
            return false;
        }
        if (storedHash.startsWith("{noop}")) {
            return storedHash.substring(6).equals(rawPassword);
        }
        if (storedHash.startsWith("$2")) {
            return passwordEncoder.matches(rawPassword, storedHash);
        }
        return storedHash.equals(rawPassword);
    }
}
