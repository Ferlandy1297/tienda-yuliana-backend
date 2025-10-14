package com.tiendayuliana.backend.service.impl;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tiendayuliana.backend.repository.ProductoRepository;
import com.tiendayuliana.backend.service.EmailService;
import com.tiendayuliana.backend.service.StockNotificationService;

@Service
public class StockNotificationServiceImpl implements StockNotificationService {
    private final ProductoRepository productoRepository;
    private final EmailService emailService;
    @Value("${app.notify.stock.to:admin@example.com}")
    private String to;

    public StockNotificationServiceImpl(ProductoRepository productoRepository, EmailService emailService) {
        this.productoRepository = productoRepository;
        this.emailService = emailService;
    }

    @Override
    public void notifyIfLowStock() {
        var productos = productoRepository.findStockBajo();
        if (productos.isEmpty()) return;
        String body = productos.stream()
                .map(p -> p.getNombre()+" ("+p.getStock()+" <= min "+p.getStockMinimo()+")")
                .collect(Collectors.joining("\n"));
        emailService.send(to, "Alertas de Stock Bajo", body);
    }
}

