package com.tiendayuliana.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findAllByOrderByFechaHoraDesc();
}
