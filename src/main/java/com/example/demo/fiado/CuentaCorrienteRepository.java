package com.example.demo.fiado;

import com.example.demo.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaCorrienteRepository extends JpaRepository<CuentaCorriente, Integer> {
    Optional<CuentaCorriente> findByCliente(Cliente cliente);
}

