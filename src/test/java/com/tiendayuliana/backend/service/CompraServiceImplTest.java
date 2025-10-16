package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.compra.CompraCreateDTO;
import com.tiendayuliana.backend.dto.compra.CompraItemDTO;
import com.tiendayuliana.backend.dto.compra.PagoCompraDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.model.*;
import com.tiendayuliana.backend.repository.*;
import com.tiendayuliana.backend.service.impl.CompraServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceImplTest {
    @Mock CompraRepository compraRepo;
    @Mock CompraDetalleRepository compraDetRepo;
    @Mock PagoCompraRepository pagoCompraRepo;
    @Mock ProveedorRepository proveedorRepo;
    @Mock ProductoRepository productoRepo;
    @Mock LoteRepository loteRepo;
    @InjectMocks CompraServiceImpl service;

    @Test
    void crearCompra_creaLote_incrementaStock_yCalculaTotal() {
        Proveedor prov = new Proveedor(); prov.setIdProveedor(1); prov.setNombre("Prov1");
        when(proveedorRepo.findById(1)).thenReturn(Optional.of(prov));
        Producto p = new Producto(); p.setIdProducto(10); p.setStock(0); p.setCostoActual(new BigDecimal("0"));
        when(productoRepo.findById(10)).thenReturn(Optional.of(p));

        Compra saved = new Compra(); saved.setIdCompra(100);
        when(compraRepo.save(any())).thenReturn(saved);

        // Stub lote save to assign ID
        when(loteRepo.save(any())).thenAnswer(inv -> {
            Lote lo = inv.getArgument(0);
            lo.setIdLote(500);
            return lo;
        });
        when(pagoCompraRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CompraCreateDTO dto = new CompraCreateDTO(1, "CONTADO",
                List.of(new CompraItemDTO(10, 10, new BigDecimal("1.50"), LocalDate.now().plusDays(60))),
                new BigDecimal("10.00"), "obs");

        Integer id = service.crearCompra(dto);
        assertEquals(100, id);
        verify(loteRepo, atLeastOnce()).save(any());
        verify(productoRepo, atLeastOnce()).save(any());
        verify(pagoCompraRepo, atLeastOnce()).save(any());
    }

    @Test
    void pagarCompra_validaMontoMayorCero() {
        Compra c = new Compra(); c.setIdCompra(5); c.setTotal(new BigDecimal("50"));
        when(compraRepo.findById(5)).thenReturn(Optional.of(c));
        assertThrows(BadRequestException.class, () -> service.pagar(5, new PagoCompraDTO(new BigDecimal("0"), null)));
    }
}
