package com.tiendayuliana.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tiendayuliana.backend.model.UsuarioSis;
import com.tiendayuliana.backend.repository.UsuarioSisRepository;

@Configuration
public class DataSeeder {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CommandLineRunner seedUsuarios(UsuarioSisRepository usuarioRepo, PasswordEncoder encoder) {
    return args -> {
      if (usuarioRepo.count() == 0) {
        UsuarioSis admin = new UsuarioSis();
        admin.setNombreUsuario("admin");
        admin.setPasswordHash(encoder.encode("admin123"));
        admin.setRol("ADMIN");
        admin.setActivo(true);
        usuarioRepo.save(admin);

        UsuarioSis empleado = new UsuarioSis();
        empleado.setNombreUsuario("empleado");
        empleado.setPasswordHash(encoder.encode("empleado123"));
        empleado.setRol("EMPLEADO");
        empleado.setActivo(true);
        usuarioRepo.save(empleado);
      }
    };
  }
}
