package com.tiendayuliana.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tiendayuliana.backend.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Integer> {

    // Si usas Java 21 puedes usar text block """...""" sin problema
    @Query("""
        select date(c.fechaHora) as fecha, sum(c.total) as total
        from Compra c
        where c.fechaHora between :inicio and :fin
          and (:idProveedor is null or c.proveedor.idProveedor = :idProveedor)
        group by date(c.fechaHora)
        order by date(c.fechaHora) asc
    """)
    List<Object[]> comprasPorPeriodo(LocalDateTime inicio, LocalDateTime fin, Integer idProveedor);
}