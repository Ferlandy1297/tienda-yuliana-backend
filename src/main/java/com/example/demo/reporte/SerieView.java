package com.example.demo.reporte;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface SerieView {
    Timestamp getPeriodo();
    BigDecimal getTotal();
}

