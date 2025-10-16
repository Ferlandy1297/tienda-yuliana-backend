package com.tiendayuliana.backend.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailFallbackConfig {

  /**
   * Fallback para entorno local: si no hay JavaMailSender configurado,
   * registra uno vacío para que el contexto arranque.
   */
  @Bean
  @ConditionalOnMissingBean(JavaMailSender.class)
  public JavaMailSender javaMailSender() {
    // Sin host/credenciales: NO enviará correo, pero permite inyección.
    return new JavaMailSenderImpl();
  }
}