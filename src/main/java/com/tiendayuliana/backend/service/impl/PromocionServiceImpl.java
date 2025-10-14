package com.tiendayuliana.backend.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.promocion.PromocionCreateDTO;
import com.tiendayuliana.backend.dto.promocion.PromocionResponseDTO;
import com.tiendayuliana.backend.model.Promocion;
import com.tiendayuliana.backend.repository.PromocionRepository;
import com.tiendayuliana.backend.service.PromocionService;

@Service
public class PromocionServiceImpl implements PromocionService {
    private final PromocionRepository repo;
    public PromocionServiceImpl(PromocionRepository repo) { this.repo = repo; }

    @Override @Transactional(readOnly = true)
    public List<PromocionResponseDTO> listar() {
        return repo.findAll().stream().map(this::map).toList();
    }

    @Override @Transactional
    public PromocionResponseDTO crear(PromocionCreateDTO dto) {
        Promocion p = new Promocion();
        p.setNombre(dto.nombre());
        p.setPorcentaje(dto.porcentaje());
        p.setDesde(dto.desde());
        p.setHasta(dto.hasta());
        if (dto.activo() != null) p.setActivo(dto.activo());
        p = repo.save(p);
        return map(p);
    }

    @Override @Transactional(readOnly = true)
    public java.util.Optional<PromocionResponseDTO> activa() {
        return repo.findActive(LocalDate.now()).map(this::map);
    }

    private PromocionResponseDTO map(Promocion p) {
        return new PromocionResponseDTO(p.getIdPromocion(), p.getNombre(), p.getPorcentaje(), p.getDesde(), p.getHasta(), p.getActivo());
    }
}

