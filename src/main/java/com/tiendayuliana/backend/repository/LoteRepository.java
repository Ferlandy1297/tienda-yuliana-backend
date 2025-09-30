// LoteRepository.java
package com.tiendayuliana.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.Lote;
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    java.util.List<Lote> findByProducto_IdProductoOrderByFechaVencimientoAsc(Integer idProducto);
}
