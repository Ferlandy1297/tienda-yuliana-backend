package com.tiendayuliana.backend.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.cc.AbonoCreateDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.exception.NotFoundException;
import com.tiendayuliana.backend.model.CuentaCorriente;
import com.tiendayuliana.backend.model.MovimientoCc;
import com.tiendayuliana.backend.repository.CuentaCorrienteRepository;
import com.tiendayuliana.backend.repository.MovimientoCcRepository;
import com.tiendayuliana.backend.service.CuentaCorrienteService;

@Service
public class CuentaCorrienteServiceImpl implements CuentaCorrienteService {

    private final CuentaCorrienteRepository cuentaRepo;
    private final MovimientoCcRepository movRepo;

    public CuentaCorrienteServiceImpl(CuentaCorrienteRepository cuentaRepo, MovimientoCcRepository movRepo) {
        this.cuentaRepo = cuentaRepo;
        this.movRepo = movRepo;
    }

    @Override
    @Transactional
    public void registrarAbono(Integer idCuenta, AbonoCreateDTO dto) {
        CuentaCorriente cuenta = cuentaRepo.findById(idCuenta)
                .orElseThrow(() -> new NotFoundException("Cuenta corriente no encontrada"));

        if (dto.monto() == null || dto.monto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Monto inválido");
        }

        // TODO: validar límite de crédito, estado de mora y bloqueo (CU-TY-008)

        MovimientoCc mov = new MovimientoCc();
        mov.setCuenta(cuenta);
        mov.setTipo("ABONO");
        mov.setMonto(dto.monto());
        // Guardar en campo referencia ya definido en entidad
        mov.setReferencia(dto.observacion());
        movRepo.save(mov);
    }
}
