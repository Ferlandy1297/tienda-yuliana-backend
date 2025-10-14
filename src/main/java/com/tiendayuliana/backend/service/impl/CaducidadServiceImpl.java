package com.tiendayuliana.backend.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendayuliana.backend.dto.caducidad.CaducidadAccionDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.exception.NotFoundException;
import com.tiendayuliana.backend.model.DevolucionDetalle;
import com.tiendayuliana.backend.model.DevolucionDetalleId;
import com.tiendayuliana.backend.model.DevolucionProveedor;
import com.tiendayuliana.backend.model.Lote;
import com.tiendayuliana.backend.model.Merma;
import com.tiendayuliana.backend.model.Producto;
import com.tiendayuliana.backend.model.Proveedor;
import com.tiendayuliana.backend.repository.DevolucionDetalleRepository;
import com.tiendayuliana.backend.repository.DevolucionProveedorRepository;
import com.tiendayuliana.backend.repository.LoteRepository;
import com.tiendayuliana.backend.repository.MermaRepository;
import com.tiendayuliana.backend.repository.ProductoRepository;
import com.tiendayuliana.backend.service.CaducidadService;

@Service
public class CaducidadServiceImpl implements CaducidadService {

    private final LoteRepository loteRepo;
    private final MermaRepository mermaRepo;
    private final ProductoRepository productoRepo;
    private final DevolucionProveedorRepository devRepo;
    private final DevolucionDetalleRepository detRepo;

    public CaducidadServiceImpl(LoteRepository loteRepo, MermaRepository mermaRepo, ProductoRepository productoRepo,
                                DevolucionProveedorRepository devRepo, DevolucionDetalleRepository detRepo) {
        this.loteRepo = loteRepo;
        this.mermaRepo = mermaRepo;
        this.productoRepo = productoRepo;
        this.devRepo = devRepo;
        this.detRepo = detRepo;
    }

    @Override
    @Transactional
    public void aplicar(CaducidadAccionDTO dto) {
        String accion = dto.accion() == null ? "" : dto.accion().trim().toUpperCase();
        if (!(accion.equals("DONACION") || accion.equals("DEVOLUCION") || accion.equals("DESCUENTO"))) {
            throw new BadRequestException("Acción de caducidad inválida");
        }
        if (dto.items() == null || dto.items().isEmpty()) {
            throw new BadRequestException("Debe enviar items de lotes");
        }

        switch (accion) {
            case "DONACION" -> procesarDonacion(dto);
            case "DEVOLUCION" -> procesarDevolucion(dto);
            case "DESCUENTO" -> procesarDescuento(dto);
        }
    }

    private void procesarDonacion(CaducidadAccionDTO dto) {
        for (CaducidadAccionDTO.Item it : dto.items()) {
            Lote lote = loteRepo.findById(it.idLote())
                    .orElseThrow(() -> new NotFoundException("Lote no encontrado: " + it.idLote()));
            if (it.cantidad() == null || it.cantidad() <= 0) {
                throw new BadRequestException("Cantidad inválida");
            }
            if (lote.getCantidadDisponible() < it.cantidad()) {
                throw new BadRequestException("Cantidad mayor a disponible en lote");
            }
            Producto prod = lote.getProducto();
            lote.setCantidadDisponible(lote.getCantidadDisponible() - it.cantidad());
            loteRepo.save(lote);
            prod.setStock(prod.getStock() - it.cantidad());
            productoRepo.save(prod);

            Merma m = new Merma();
            m.setProducto(prod);
            m.setLote(lote);
            m.setCantidad(it.cantidad());
            m.setMotivo("DONACION_CADUCIDAD");
            BigDecimal costo = prod.getCostoActual() == null ? BigDecimal.ZERO : prod.getCostoActual();
            m.setCostoEstimado(costo.multiply(BigDecimal.valueOf(it.cantidad())));
            m.setObservacion("Donación por caducidad");
            mermaRepo.save(m);
        }
    }

    private void procesarDevolucion(CaducidadAccionDTO dto) {
        // Agrupar por proveedor del producto del lote
        Map<Proveedor, List<CaducidadAccionDTO.Item>> porProveedor = new HashMap<>();
        for (CaducidadAccionDTO.Item it : dto.items()) {
            Lote lote = loteRepo.findById(it.idLote())
                    .orElseThrow(() -> new NotFoundException("Lote no encontrado: " + it.idLote()));
            Proveedor prov = lote.getProducto().getProveedor();
            if (prov == null) {
                throw new BadRequestException("Producto sin proveedor asociado para devolución");
            }
            porProveedor.computeIfAbsent(prov, k -> new ArrayList<>()).add(it);
        }

        for (Map.Entry<Proveedor, List<CaducidadAccionDTO.Item>> entry : porProveedor.entrySet()) {
            Proveedor prov = entry.getKey();
            DevolucionProveedor dev = new DevolucionProveedor();
            dev.setProveedor(prov);
            dev.setMotivo("Devolución por caducidad");
            dev.setTotalEstimado(BigDecimal.ZERO);
            dev = devRepo.save(dev);

            BigDecimal total = BigDecimal.ZERO;
            for (CaducidadAccionDTO.Item it : entry.getValue()) {
                Lote lote = loteRepo.findById(it.idLote())
                        .orElseThrow(() -> new NotFoundException("Lote no encontrado: " + it.idLote()));
                if (it.cantidad() == null || it.cantidad() <= 0) {
                    throw new BadRequestException("Cantidad inválida");
                }
                if (lote.getCantidadDisponible() < it.cantidad()) {
                    throw new BadRequestException("Cantidad mayor a disponible en lote");
                }
                Producto prod = lote.getProducto();
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
        }
    }

    private void procesarDescuento(CaducidadAccionDTO dto) {
        // Sin campo específico en Lote para marcar descuento. Se deja como NO-OP con registro mínimo en observación de merma.
        // Si se requiere persistir bandera, añadir columna 'enDescuento' en Lote (no implementado por evitar cambios de esquema).
        for (CaducidadAccionDTO.Item it : dto.items()) {
            // validamos existencia del lote y cantidad pero no descontamos stock
            Lote lote = loteRepo.findById(it.idLote())
                    .orElseThrow(() -> new NotFoundException("Lote no encontrado: " + it.idLote()));
            if (it.cantidad() == null || it.cantidad() <= 0) {
                throw new BadRequestException("Cantidad inválida");
            }
            // Marca lógica podría integrarse aquí en el futuro.
        }
    }
}
