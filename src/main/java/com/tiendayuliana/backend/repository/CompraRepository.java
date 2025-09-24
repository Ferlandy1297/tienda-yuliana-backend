// repository/CompraRepository.java
package com.tiendayuliana.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.Compra;
public interface CompraRepository extends JpaRepository<Compra, Integer> {}
