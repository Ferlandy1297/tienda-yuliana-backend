package com.tiendayuliana.backend.service;

public interface EmailService {
    void send(String to, String subject, String body);
}

