package com.tiendayuliana.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    Optional<Pago> findFirstByVenta_IdVentaOrderByFechaHoraAsc(Integer idVenta);
}
