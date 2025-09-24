// UsuarioSisRepository.java
package com.tiendayuliana.backend.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.UsuarioSis;
public interface UsuarioSisRepository extends JpaRepository<UsuarioSis, Integer> {
    Optional<UsuarioSis> findByNombreUsuario(String nombreUsuario);
}
