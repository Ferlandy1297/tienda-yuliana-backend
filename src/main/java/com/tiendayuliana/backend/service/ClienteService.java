package com.tiendayuliana.backend.service;

import java.util.List;

import com.tiendayuliana.backend.dto.cliente.ClienteCreateDTO;
import com.tiendayuliana.backend.dto.cliente.ClienteResponseDTO;
import com.tiendayuliana.backend.dto.cliente.ClienteUpdateDTO;

public interface ClienteService {
    List<ClienteResponseDTO> listar();
    ClienteResponseDTO getById(Integer id);
    ClienteResponseDTO crear(ClienteCreateDTO dto);
    ClienteResponseDTO actualizar(Integer id, ClienteUpdateDTO dto);
    void eliminar(Integer id);
}

