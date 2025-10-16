package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.ProductCreateDTO;
import com.tiendayuliana.backend.model.Producto;
import com.tiendayuliana.backend.repository.ProductoRepository;
import com.tiendayuliana.backend.repository.ProveedorRepository;
import com.tiendayuliana.backend.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {
    @Mock ProductoRepository productoRepository;
    @Mock ProveedorRepository proveedorRepository;
    @InjectMocks ProductoServiceImpl service;

    @Test
    void stockBajo_devuelveProductosConStockMenorOIgualAlMinimo() {
        Producto p = new Producto(); p.setIdProducto(1); p.setNombre("P1"); p.setStock(5); p.setStockMinimo(10);
        when(productoRepository.findStockBajo()).thenReturn(List.of(p));
        var list = service.stockBajo();
        assertEquals(1, list.size());
        assertEquals("P1", list.get(0).nombre());
    }
}

