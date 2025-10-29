package com.example.demo.reporte;

import java.math.BigDecimal;

public interface TopProductoView {
    Integer getIdProducto();
    String getNombre();
    Long getCantidad();
    BigDecimal getTotal();
}

