package com.tiendayuliana.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.VentaDetalle;
import com.tiendayuliana.backend.model.VentaDetalleId;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, VentaDetalleId> {
    List<VentaDetalle> findAllByVenta_IdVenta(Integer idVenta);
}
