package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioSisRepository extends JpaRepository<UsuarioSis, Integer> {
    Optional<UsuarioSis> findByNombreUsuarioAndActivoTrue(String nombreUsuario);
}

