package com.example.demo.reporte;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReporteNativeRepository extends Repository<com.example.demo.venta.Venta, Integer> {

    @Query(value = "select vd.id_producto as idProducto, p.nombre as nombre, sum(vd.cantidad) as cantidad, sum(vd.cantidad*vd.precio_unitario - vd.descuento) as total " +
            "from tienda_yuliana.venta_detalle vd " +
            "join tienda_yuliana.venta v on v.id_venta = vd.id_venta " +
            "join tienda_yuliana.producto p on p.id_producto = vd.id_producto " +
            "where v.fecha_hora between :start and :end " +
            "group by vd.id_producto, p.nombre order by cantidad desc", nativeQuery = true)
    List<TopProductoView> topVendidos(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end, Pageable pageable);

    @Query(value = "select coalesce(sum(vd.cantidad*vd.precio_unitario - vd.descuento),0) as totalVentas, " +
            "coalesce(sum(vd.cantidad * cd.costo_unitario),0) as costoVentas " +
            "from tienda_yuliana.venta_detalle vd " +
            "join tienda_yuliana.venta v on v.id_venta = vd.id_venta " +
            "left join tienda_yuliana.compra_detalle cd on cd.id_lote = vd.id_lote " +
            "where v.fecha_hora between :start and :end", nativeQuery = true)
    ProfitView utilidades(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);

    @Query(value = "select date_trunc(:gran, v.fecha_hora) as periodo, coalesce(sum(vd.cantidad*vd.precio_unitario - vd.descuento),0) as total " +
            "from tienda_yuliana.venta_detalle vd join tienda_yuliana.venta v on v.id_venta = vd.id_venta " +
            "where v.fecha_hora between :start and :end " +
            "group by periodo order by periodo", nativeQuery = true)
    List<SerieView> serie(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end, @Param("gran") String gran);
}

