package com.tiendayuliana.backend.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.merma.MermaCreateDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.exception.NotFoundException;
import com.tiendayuliana.backend.model.Merma;
import com.tiendayuliana.backend.model.Producto;
import com.tiendayuliana.backend.repository.MermaRepository;
import com.tiendayuliana.backend.repository.ProductoRepository;
import com.tiendayuliana.backend.service.MermaService;

@Service
public class MermaServiceImpl implements MermaService {

    private final ProductoRepository productoRepository;
    private final MermaRepository mermaRepository;
    private final com.tiendayuliana.backend.service.StockNotificationService stockNotificationService;

    public MermaServiceImpl(ProductoRepository productoRepository, MermaRepository mermaRepository,
                            com.tiendayuliana.backend.service.StockNotificationService stockNotificationService) {
        this.productoRepository = productoRepository;
        this.mermaRepository = mermaRepository;
        this.stockNotificationService = stockNotificationService;
    }

    @Override
    @Transactional
    public void registrar(MermaCreateDTO dto) {
        Producto prod = productoRepository.findById(dto.idProducto())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (dto.cantidad() == null || dto.cantidad() <= 0) {
            throw new BadRequestException("Cantidad inválida");
        }
        if (prod.getStock() < dto.cantidad()) {
            throw new BadRequestException("Stock insuficiente para merma");
        }

        prod.setStock(prod.getStock() - dto.cantidad());
        productoRepository.save(prod);

        Merma m = new Merma();
        m.setProducto(prod);
        m.setCantidad(dto.cantidad());
        m.setMotivo(dto.motivo() == null ? "MERMA" : dto.motivo());
        // estimar costo
        BigDecimal costo = prod.getCostoActual() == null ? BigDecimal.ZERO : prod.getCostoActual();
        m.setCostoEstimado(costo.multiply(BigDecimal.valueOf(dto.cantidad())));
        mermaRepository.save(m);
        stockNotificationService.notifyIfLowStock();
    }
}
