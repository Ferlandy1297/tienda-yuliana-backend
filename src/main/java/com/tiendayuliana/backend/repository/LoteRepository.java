
package com.tiendayuliana.backend.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tiendayuliana.backend.model.Lote;
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    java.util.List<Lote> findByProducto_IdProductoOrderByFechaVencimientoAsc(Integer idProducto);

    @Query("select l from Lote l where l.fechaVencimiento between :desde and :hasta and l.cantidadDisponible > 0 order by l.fechaVencimiento asc")
    List<Lote> findVencenEntre(LocalDate desde, LocalDate hasta);
}
