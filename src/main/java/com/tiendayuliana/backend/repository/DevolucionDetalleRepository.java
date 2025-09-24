// repository/DevolucionDetalleRepository.java
package com.tiendayuliana.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.DevolucionDetalle;
import com.tiendayuliana.backend.model.DevolucionDetalleId;
public interface DevolucionDetalleRepository extends JpaRepository<DevolucionDetalle, DevolucionDetalleId> {}
