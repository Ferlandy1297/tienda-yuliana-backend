package com.example.demo.producto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    boolean existsByCodigoBarras(String codigoBarras);
    Optional<Producto> findByCodigoBarras(String codigoBarras);
}

