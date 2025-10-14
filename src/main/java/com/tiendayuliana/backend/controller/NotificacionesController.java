package com.tiendayuliana.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.service.StockNotificationService;

@RestController
@RequestMapping("/api/notificaciones/stock-bajo")
public class NotificacionesController {
    private final StockNotificationService stockNotificationService;
    public NotificacionesController(StockNotificationService stockNotificationService) { this.stockNotificationService = stockNotificationService; }

    @PostMapping("/send")
    @PreAuthorize("@roles.has('ADMIN')")
    public java.util.Map<String, String> send() {
        stockNotificationService.notifyIfLowStock();
        return java.util.Map.of("status","ENVIADO");
    }
}

