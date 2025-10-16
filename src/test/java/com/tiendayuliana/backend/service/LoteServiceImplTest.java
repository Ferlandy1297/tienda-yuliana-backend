package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.model.Lote;
import com.tiendayuliana.backend.model.Producto;
import com.tiendayuliana.backend.repository.LoteRepository;
import com.tiendayuliana.backend.service.impl.LoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoteServiceImplTest {
    @Mock LoteRepository loteRepository;
    @InjectMocks LoteServiceImpl service;

    @Test
    void porVencer_mapeaDTOCorrectamente() {
        Producto prod = new Producto(); prod.setIdProducto(99); prod.setNombre("Yogurt");
        Lote l = new Lote(5, prod, LocalDate.now().plusDays(10), 7);
        when(loteRepository.findVencenEntre(any(), any())).thenReturn(List.of(l));
        var out = service.porVencer(30);
        assertEquals(1, out.size());
        assertEquals(5, out.get(0).idLote());
        assertEquals(99, out.get(0).idProducto());
        assertEquals("Yogurt", out.get(0).nombreProducto());
    }
}
