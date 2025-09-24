// repository/CompraDetalleRepository.java
package com.tiendayuliana.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.CompraDetalle;
import com.tiendayuliana.backend.model.CompraDetalleId;
public interface CompraDetalleRepository extends JpaRepository<CompraDetalle, CompraDetalleId> {}
