package com.example.demo.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByFechaHoraBetween(OffsetDateTime start, OffsetDateTime end);

    @Query("select coalesce(sum(v.total),0) from Venta v where v.fechaHora between :start and :end")
    java.math.BigDecimal totalBetween(OffsetDateTime start, OffsetDateTime end);
}

