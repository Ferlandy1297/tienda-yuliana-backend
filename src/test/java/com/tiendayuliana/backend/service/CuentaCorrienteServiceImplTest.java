package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.cc.AbonoCreateDTO;
import com.tiendayuliana.backend.model.CuentaCorriente;
import com.tiendayuliana.backend.model.MovimientoCc;
import com.tiendayuliana.backend.repository.CuentaCorrienteRepository;
import com.tiendayuliana.backend.repository.MovimientoCcRepository;
import com.tiendayuliana.backend.service.impl.CuentaCorrienteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaCorrienteServiceImplTest {

    @Mock CuentaCorrienteRepository cuentaRepo;
    @Mock MovimientoCcRepository movRepo;
    @InjectMocks CuentaCorrienteServiceImpl service;

    @Test
    void registrarAbono_creaMovimientoTipoABONO() {
        CuentaCorriente c = new CuentaCorriente();
        c.setIdCuenta(7);
        when(cuentaRepo.findById(7)).thenReturn(Optional.of(c));

        AbonoCreateDTO dto = new AbonoCreateDTO(new BigDecimal("50.00"), "abono test");
        service.registrarAbono(7, dto);

        ArgumentCaptor<MovimientoCc> cap = ArgumentCaptor.forClass(MovimientoCc.class);
        verify(movRepo, times(1)).save(cap.capture());
        MovimientoCc saved = cap.getValue();
        assertEquals("ABONO", saved.getTipo());
        assertEquals(new BigDecimal("50.00"), saved.getMonto());
        assertEquals("abono test", saved.getReferencia());
    }
}

