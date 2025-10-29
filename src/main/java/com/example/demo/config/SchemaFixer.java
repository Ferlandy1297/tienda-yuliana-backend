package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class SchemaFixer {
    @Bean
    CommandLineRunner ensureColumns(DataSource dataSource) {
        return args -> {
            try (Connection c = dataSource.getConnection(); Statement st = c.createStatement()) {
                st.executeUpdate("ALTER TABLE tienda_yuliana.cliente ADD COLUMN IF NOT EXISTS nit VARCHAR(30)");
            } catch (Exception ignored) { }
        };
    }
}

