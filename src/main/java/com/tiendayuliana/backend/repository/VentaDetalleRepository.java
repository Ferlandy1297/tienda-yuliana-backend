package com.tiendayuliana.backend.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.VentaDetalle;
import com.tiendayuliana.backend.model.VentaDetalleId;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, VentaDetalleId> {
    List<VentaDetalle> findAllByVenta_IdVenta(Integer idVenta);

    @org.springframework.data.jpa.repository.Query("""
        select d from VentaDetalle d where d.venta.fechaHora between :desde and :hasta
    """)
    List<VentaDetalle> findAllBetween(LocalDateTime desde, LocalDateTime hasta);

    @org.springframework.data.jpa.repository.Query("""
        select d.producto.idProducto, sum(d.cantidad) as cant
        from VentaDetalle d
        where d.venta.fechaHora between :desde and :hasta
        group by d.producto.idProducto
        order by sum(d.cantidad) desc
    """)
    List<Object[]> topProductos(LocalDateTime desde, LocalDateTime hasta);
}
