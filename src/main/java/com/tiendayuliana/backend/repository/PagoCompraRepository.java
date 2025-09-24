// repository/PagoCompraRepository.java
package com.tiendayuliana.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.PagoCompra;
public interface PagoCompraRepository extends JpaRepository<PagoCompra, Integer> {}
