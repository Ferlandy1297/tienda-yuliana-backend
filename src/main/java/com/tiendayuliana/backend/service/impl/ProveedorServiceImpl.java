package com.tiendayuliana.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.proveedor.ProveedorCreateDTO;
import com.tiendayuliana.backend.dto.proveedor.ProveedorResponseDTO;
import com.tiendayuliana.backend.dto.proveedor.ProveedorUpdateDTO;
import com.tiendayuliana.backend.exception.NotFoundException;
import com.tiendayuliana.backend.model.Proveedor;
import com.tiendayuliana.backend.repository.ProveedorRepository;
import com.tiendayuliana.backend.service.ProveedorService;

@Service
public class ProveedorServiceImpl implements ProveedorService {
    private final ProveedorRepository repo;
    public ProveedorServiceImpl(ProveedorRepository repo) { this.repo = repo; }

    @Override @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listar() {
        return repo.findAll().stream().map(this::map).toList();
    }

    @Override @Transactional(readOnly = true)
    public ProveedorResponseDTO getById(Integer id) {
        Proveedor p = repo.findById(id).orElseThrow(() -> new NotFoundException("Proveedor no encontrado"));
        return map(p);
    }

    @Override @Transactional
    public ProveedorResponseDTO crear(ProveedorCreateDTO dto) {
        Proveedor p = new Proveedor();
        p.setNombre(dto.nombre());
        p.setContacto(dto.contacto());
        p.setTelefono(dto.telefono());
        p = repo.save(p);
        return map(p);
    }

    @Override @Transactional
    public ProveedorResponseDTO actualizar(Integer id, ProveedorUpdateDTO dto) {
        Proveedor p = repo.findById(id).orElseThrow(() -> new NotFoundException("Proveedor no encontrado"));
        p.setNombre(dto.nombre());
        p.setContacto(dto.contacto());
        p.setTelefono(dto.telefono());
        if (dto.activo() != null) p.setActivo(dto.activo());
        p = repo.save(p);
        return map(p);
    }

    @Override @Transactional
    public void eliminar(Integer id) {
        Proveedor p = repo.findById(id).orElseThrow(() -> new NotFoundException("Proveedor no encontrado"));
        p.setActivo(false);
        repo.save(p);
    }

    private ProveedorResponseDTO map(Proveedor p) {
        return new ProveedorResponseDTO(p.getIdProveedor(), p.getNombre(), p.getContacto(), p.getTelefono(), p.getActivo());
    }
}

