package com.tiendayuliana.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.cliente.ClienteCreateDTO;
import com.tiendayuliana.backend.dto.cliente.ClienteResponseDTO;
import com.tiendayuliana.backend.dto.cliente.ClienteUpdateDTO;
import com.tiendayuliana.backend.exception.NotFoundException;
import com.tiendayuliana.backend.model.Cliente;
import com.tiendayuliana.backend.repository.ClienteRepository;
import com.tiendayuliana.backend.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

    public ClienteServiceImpl(ClienteRepository repo) { this.repo = repo; }

    @Override @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {
        return repo.findAll().stream().map(this::map).toList();
    }

    @Override @Transactional(readOnly = true)
    public ClienteResponseDTO getById(Integer id) {
        Cliente c = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        return map(c);
    }

    @Override @Transactional
    public ClienteResponseDTO crear(ClienteCreateDTO dto) {
        Cliente c = new Cliente();
        c.setNombre(dto.nombre());
        c.setTelefono(dto.telefono());
        c.setNit(dto.nit());
        c.setEsMayorista(dto.esMayorista());
        c.setLimiteCredito(dto.limiteCredito());
        c = repo.save(c);
        return map(c);
    }

    @Override @Transactional
    public ClienteResponseDTO actualizar(Integer id, ClienteUpdateDTO dto) {
        Cliente c = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        c.setNombre(dto.nombre());
        c.setTelefono(dto.telefono());
        c.setNit(dto.nit());
        c.setEsMayorista(dto.esMayorista());
        c.setLimiteCredito(dto.limiteCredito());
        if (dto.activo() != null) c.setActivo(dto.activo());
        c = repo.save(c);
        return map(c);
    }

    @Override @Transactional
    public void eliminar(Integer id) {
        Cliente c = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        c.setActivo(false);
        repo.save(c);
    }

    private ClienteResponseDTO map(Cliente c) {
        return new ClienteResponseDTO(
                c.getIdCliente(), c.getNombre(), c.getTelefono(), c.getNit(),
                c.isEsMayorista(), c.getLimiteCredito(), c.getEstadoCredito(), c.getActivo()
        );
    }
}

