// repository/MovimientoCcRepository.java
package com.tiendayuliana.backend.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tiendayuliana.backend.model.MovimientoCc;

public interface MovimientoCcRepository extends JpaRepository<MovimientoCc, Integer> {
    @Query("select coalesce(sum(case when m.tipo = 'CARGO' then m.monto else -m.monto end), 0) from MovimientoCc m where m.cuenta.idCuenta = :idCuenta")
    BigDecimal saldoActual(Integer idCuenta);
}
