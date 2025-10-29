package com.example.demo.compra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
    List<Compra> findByFechaHoraBetween(OffsetDateTime inicio, OffsetDateTime fin);

    @Query("select coalesce(sum(c.total),0) from Compra c where c.fechaHora between :inicio and :fin")
    java.math.BigDecimal totalBetween(OffsetDateTime inicio, OffsetDateTime fin);
}

