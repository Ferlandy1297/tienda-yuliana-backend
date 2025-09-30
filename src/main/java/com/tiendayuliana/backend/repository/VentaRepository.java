package com.tiendayuliana.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

import com.tiendayuliana.backend.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findAllByOrderByFechaHoraDesc();

    @Query("""
      select date(v.fechaHora) as fecha, sum(v.total) as total
      from Venta v
      where v.fechaHora between :inicio and :fin
      group by date(v.fechaHora)
      order by date(v.fechaHora) asc
    """)
    java.util.List<Object[]> ventasPorPeriodo(LocalDateTime inicio, LocalDateTime fin);
}

