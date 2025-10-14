package com.tiendayuliana.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tiendayuliana.backend.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    Optional<Producto> findByCodigoBarras(String codigoBarras);

    Optional<Producto> findByCodigoBarrasAndActivoTrue(String codigoBarras);

    List<Producto> findByActivoTrue();

    @Query("select p from Producto p where p.stock <= p.stockMinimo and p.activo = true")
    List<Producto> findStockBajo();
}
