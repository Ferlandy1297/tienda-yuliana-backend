package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.venta.PagoCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaDetalleCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaResponseDTO;
import com.tiendayuliana.backend.model.*;
import com.tiendayuliana.backend.repository.*;
import com.tiendayuliana.backend.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class VentaServiceImplTest {

    @Mock VentaRepository ventaRepository;
    @Mock VentaDetalleRepository ventaDetalleRepository;
    @Mock PagoRepository pagoRepository;
    @Mock ProductoRepository productoRepository;
    @Mock LoteRepository loteRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock UsuarioSisRepository usuarioSisRepository;
    @Mock PromocionRepository promocionRepository;
    @Mock CuentaCorrienteRepository cuentaCorrienteRepository;
    @Mock MovimientoCcRepository movimientoCcRepository;
    @Mock StockNotificationService stockNotificationService;

    @InjectMocks VentaServiceImpl service;

    @Test
    void crearVentaDetalle_conPago_efectivoCalculaCambio_yDescuentaStockPorLote() {
        UsuarioSis u = new UsuarioSis(1, "user", "{noop}x", "EMPLEADO", true);
        when(usuarioSisRepository.findById(1)).thenReturn(Optional.of(u));

        Producto p = new Producto();
        p.setIdProducto(10);
        p.setNombre("Galletas");
        p.setPrecioVenta(new BigDecimal("2.50"));
        p.setStock(10);
        when(productoRepository.findById(10)).thenReturn(Optional.of(p));

        Lote l = new Lote(100, p, LocalDate.now().plusDays(30), 5);
        when(loteRepository.findById(100)).thenReturn(Optional.of(l));

        Venta vSaved = new Venta();
        vSaved.setIdVenta(999);
        vSaved.setUsuario(u);
        when(ventaRepository.save(any())).thenReturn(vSaved);

        when(promocionRepository.findActive(any())).thenReturn(Optional.empty());

        VentaCreateDTO dto = new VentaCreateDTO();
        dto.setTipo("DETALLE");
        dto.setIdUsuario(1);
        VentaDetalleCreateDTO d = new VentaDetalleCreateDTO();
        d.setIdProducto(10); d.setIdLote(100); d.setCantidad(2);
        dto.setDetalles(List.of(d));
        PagoCreateDTO pago = new PagoCreateDTO();
        pago.setMontoEntregado(new BigDecimal("10"));
        pago.setDenominacionBillete(new BigDecimal("10"));
        dto.setPago(pago);

        // Stub save(pago) to return ID
        when(pagoRepository.save(any())).thenAnswer(inv -> {
            Pago pg = inv.getArgument(0);
            pg.setIdPago(1);
            return pg;
        });

        VentaResponseDTO out = service.crear(dto);

        assertEquals(999, out.idVenta());
        assertEquals(new BigDecimal("5.00"), out.total());
        assertNotNull(out.pago());
        assertEquals(new BigDecimal("5.00"), out.pago().cambioCalculado());

        // Verifica descuento de stock y lote
        assertEquals(3, l.getCantidadDisponible());
        assertEquals(8, p.getStock());
        verify(loteRepository, atLeastOnce()).save(l);
        verify(productoRepository, atLeastOnce()).save(p);
        verify(pagoRepository, atLeastOnce()).save(any());
    }

    @Test
    void crearVentaMayoreo_aplicaDescuentoSimple() {
        UsuarioSis u = new UsuarioSis(1, "user", "{noop}x", "EMPLEADO", true);
        when(usuarioSisRepository.findById(1)).thenReturn(Optional.of(u));

        Cliente c = new Cliente();
        c.setIdCliente(5); c.setEsMayorista(true);
        when(clienteRepository.findById(5)).thenReturn(Optional.of(c));

        Producto p = new Producto(); p.setIdProducto(10); p.setPrecioVenta(new BigDecimal("100")); p.setStock(20);
        when(productoRepository.findById(10)).thenReturn(Optional.of(p));

        // Hay 2 lotes FIFO suficientes
        Lote l1 = new Lote(1, p, LocalDate.now().plusDays(10), 8);
        Lote l2 = new Lote(2, p, LocalDate.now().plusDays(30), 20);
        when(loteRepository.findByProducto_IdProductoOrderByFechaVencimientoAsc(10))
                .thenReturn(List.of(l1, l2));

        Venta vSaved = new Venta(); vSaved.setIdVenta(111); vSaved.setUsuario(u);
        when(ventaRepository.save(any())).thenReturn(vSaved);

        VentaCreateDTO dto = new VentaCreateDTO();
        dto.setTipo("MAYOREO"); dto.setIdUsuario(1); dto.setIdCliente(5);
        VentaDetalleCreateDTO d = new VentaDetalleCreateDTO();
        d.setIdProducto(10); d.setCantidad(10);
        dto.setDetalles(List.of(d));
        PagoCreateDTO pago = new PagoCreateDTO(); pago.setMontoEntregado(new BigDecimal("1000"));
        dto.setPago(pago);

        when(pagoRepository.save(any())).thenAnswer(inv -> {
            Pago pg = inv.getArgument(0);
            pg.setIdPago(2);
            return pg;
        });
        VentaResponseDTO out = service.crear(dto);
        // 10% descuento sobre 100 -> 90 * 10 = 900
        assertEquals(new BigDecimal("900.0"), out.total());
    }
}
