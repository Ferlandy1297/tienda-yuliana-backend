package com.tiendayuliana.backend.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tiendayuliana.backend.model.Promocion;

public interface PromocionRepository extends JpaRepository<Promocion, Integer> {
    @Query("select p from Promocion p where p.activo = true and (p.desde is null or p.desde <= :hoy) and (p.hasta is null or p.hasta >= :hoy) order by p.idPromocion desc")
    Optional<Promocion> findActive(LocalDate hoy);
}

