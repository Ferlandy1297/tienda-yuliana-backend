package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.caducidad.CaducidadAccionDTO;
import com.tiendayuliana.backend.model.Lote;
import com.tiendayuliana.backend.repository.*;
import com.tiendayuliana.backend.service.impl.CaducidadServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaducidadServiceImplTest {
    @Mock LoteRepository loteRepository;
    @Mock MermaRepository mermaRepository;
    @Mock ProductoRepository productoRepository;
    @Mock DevolucionProveedorRepository devRepo;
    @Mock DevolucionDetalleRepository detRepo;

    @InjectMocks CaducidadServiceImpl service;

    @Test
    void descuento_marcaLoteConBanderaYPorcentaje() {
        Lote lote = new Lote(1, new com.tiendayuliana.backend.model.Producto(), LocalDate.now().plusDays(10), 5);
        when(loteRepository.findById(1)).thenReturn(Optional.of(lote));
        when(loteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var item = new CaducidadAccionDTO.Item(1, 1);
        var dto = new CaducidadAccionDTO("DESCUENTO", List.of(item), new BigDecimal("0.2"));

        service.aplicar(dto);
        assertEquals(Boolean.TRUE, lote.getEnDescuento());
        assertEquals(0, new BigDecimal("0.2").compareTo(lote.getPorcentajeDescuento()));
        verify(loteRepository, atLeastOnce()).save(lote);
    }
}

