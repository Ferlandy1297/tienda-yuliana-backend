package com.tiendayuliana.backend.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tiendayuliana.backend.model.PagoCompra;

public interface PagoCompraRepository extends JpaRepository<PagoCompra, Integer> {

    @Query("select coalesce(sum(p.monto), 0) from PagoCompra p where p.compra.idCompra = :idCompra")
    Optional<BigDecimal> sumByCompraId(Integer idCompra);
}