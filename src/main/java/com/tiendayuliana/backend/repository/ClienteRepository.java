package com.tiendayuliana.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.Cliente;
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
