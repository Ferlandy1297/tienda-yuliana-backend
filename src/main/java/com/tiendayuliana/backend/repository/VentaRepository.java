package com.tiendayuliana.backend.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tiendayuliana.backend.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findAllByOrderByFechaHoraDesc();

    @Query("""
      select date(v.fechaHora) as fecha, sum(v.total) as total, count(v)
      from Venta v
      where v.fechaHora between :inicio and :fin
      group by date(v.fechaHora)
      order by date(v.fechaHora) asc
    """)
    List<Object[]> ventasPorPeriodo(LocalDateTime inicio, LocalDateTime fin);

    @Query("select coalesce(sum(v.total), 0) from Venta v where v.fechaHora between :inicio and :fin")
    BigDecimal totalEntreFechas(LocalDateTime inicio, LocalDateTime fin);

    @Query("select count(v) from Venta v where v.fechaHora between :inicio and :fin")
    long totalVentasEntreFechas(LocalDateTime inicio, LocalDateTime fin);
}

