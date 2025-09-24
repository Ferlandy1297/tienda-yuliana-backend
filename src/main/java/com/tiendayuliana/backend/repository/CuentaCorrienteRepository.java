package com.tiendayuliana.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.CuentaCorriente;
public interface CuentaCorrienteRepository extends JpaRepository<CuentaCorriente, Integer> {
    
}
