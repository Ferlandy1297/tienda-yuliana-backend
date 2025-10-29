package com.example.demo.lote;

import com.example.demo.producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoteRepository extends JpaRepository<Lote, Integer> {
    List<Lote> findByProductoOrderByFechaVencimientoAscIdLoteAsc(Producto producto);
}

