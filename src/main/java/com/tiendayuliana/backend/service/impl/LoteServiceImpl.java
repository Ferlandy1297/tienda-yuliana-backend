package com.tiendayuliana.backend.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.lote.LotePorVencerDTO;
import com.tiendayuliana.backend.model.Lote;
import com.tiendayuliana.backend.repository.LoteRepository;
import com.tiendayuliana.backend.service.LoteService;

@Service
public class LoteServiceImpl implements LoteService {

    private final LoteRepository loteRepository;

    public LoteServiceImpl(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotePorVencerDTO> porVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);
        return loteRepository.findVencenEntre(hoy, limite).stream()
                .map(l -> new LotePorVencerDTO(
                        l.getIdLote(),
                        l.getProducto().getIdProducto(),
                        l.getProducto().getNombre(),
                        l.getFechaVencimiento(),
                        l.getCantidadDisponible()
                ))
                .toList();
    }
}
