package com.tiendayuliana.backend.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.devolucion.DevolucionCreateDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.exception.NotFoundException;
import com.tiendayuliana.backend.model.DevolucionDetalle;
import com.tiendayuliana.backend.model.DevolucionDetalleId;
import com.tiendayuliana.backend.model.DevolucionProveedor;
import com.tiendayuliana.backend.model.Lote;
import com.tiendayuliana.backend.model.Producto;
import com.tiendayuliana.backend.model.Proveedor;
import com.tiendayuliana.backend.repository.DevolucionDetalleRepository;
import com.tiendayuliana.backend.repository.DevolucionProveedorRepository;
import com.tiendayuliana.backend.repository.LoteRepository;
import com.tiendayuliana.backend.repository.ProductoRepository;
import com.tiendayuliana.backend.repository.ProveedorRepository;
import com.tiendayuliana.backend.service.DevolucionProveedorService;

@Service
public class DevolucionProveedorServiceImpl implements DevolucionProveedorService {

    private final DevolucionProveedorRepository devRepo;
    private final DevolucionDetalleRepository detRepo;
    private final ProveedorRepository provRepo;
    private final ProductoRepository productoRepo;
    private final LoteRepository loteRepo;

    public DevolucionProveedorServiceImpl(DevolucionProveedorRepository devRepo, DevolucionDetalleRepository detRepo,
                                          ProveedorRepository provRepo, ProductoRepository productoRepo,
                                          LoteRepository loteRepo) {
        this.devRepo = devRepo;
        this.detRepo = detRepo;
        this.provRepo = provRepo;
        this.productoRepo = productoRepo;
        this.loteRepo = loteRepo;
    }

    @Override
    @Transactional
    public Integer registrar(DevolucionCreateDTO dto) {
        Proveedor prov = provRepo.findById(dto.idProveedor())
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado"));
        if (dto.items() == null || dto.items().isEmpty()) {
            throw new BadRequestException("Debe indicar items a devolver");
        }

        DevolucionProveedor dev = new DevolucionProveedor();
        dev.setProveedor(prov);
        dev.setMotivo(dto.motivo());
        dev.setTotalEstimado(BigDecimal.ZERO);
        dev = devRepo.save(dev);

        BigDecimal total = BigDecimal.ZERO;
        for (DevolucionCreateDTO.Item it : dto.items()) {
            Producto prod = productoRepo.findById(it.idProducto())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + it.idProducto()));
            Lote lote = loteRepo.findById(it.idLote())
                    .orElseThrow(() -> new NotFoundException("Lote no encontrado: " + it.idLote()));
            if (!lote.getProducto().getIdProducto().equals(prod.getIdProducto())) {
                throw new BadRequestException("El lote no pertenece al producto");
            }
            if (it.cantidad() == null || it.cantidad() <= 0) {
                throw new BadRequestException("Cantidad inválida");
            }
            if (lote.getCantidadDisponible() < it.cantidad()) {
                throw new BadRequestException("Cantidad a devolver supera disponible del lote");
            }

            // Descontar del lote y del stock global
            lote.setCantidadDisponible(lote.getCantidadDisponible() - it.cantidad());
            loteRepo.save(lote);
            prod.setStock(prod.getStock() - it.cantidad());
            productoRepo.save(prod);

            DevolucionDetalleId detId = new DevolucionDetalleId();
            detId.setIdDevolucion(dev.getIdDevolucion());
            detId.setIdProducto(prod.getIdProducto());
            detId.setIdLote(lote.getIdLote());

            DevolucionDetalle det = new DevolucionDetalle();
            det.setId(detId);
            det.setDevolucion(dev);
            det.setProducto(prod);
            det.setLote(lote);
            det.setCantidad(it.cantidad());

            BigDecimal costo = prod.getCostoActual() == null ? BigDecimal.ZERO : prod.getCostoActual();
            BigDecimal costoEst = costo.multiply(BigDecimal.valueOf(it.cantidad()));
            det.setCostoEstimado(costoEst);
            detRepo.save(det);

            total = total.add(costoEst);
        }

        dev.setTotalEstimado(total);
        devRepo.save(dev);
        return dev.getIdDevolucion();
    }
}
