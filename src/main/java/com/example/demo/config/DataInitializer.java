package com.example.demo.config;

import com.example.demo.user.UsuarioSis;
import com.example.demo.user.UsuarioSisRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initAdmin(UsuarioSisRepository usuarioRepo, PasswordEncoder encoder) {
        return args -> {
            if (usuarioRepo.findByNombreUsuarioAndActivoTrue("admin").isEmpty()) {
                UsuarioSis u = new UsuarioSis();
                u.setNombreUsuario("admin");
                u.setPasswordHash(encoder.encode("admin123"));
                u.setRol("ADMIN");
                u.setActivo(true);
                usuarioRepo.save(u);
            }
        };
    }
}

