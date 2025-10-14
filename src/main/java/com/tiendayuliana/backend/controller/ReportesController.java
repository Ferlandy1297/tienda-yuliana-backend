package com.tiendayuliana.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.reportes.ReporteComprasDTO;
import com.tiendayuliana.backend.dto.reportes.ReporteVentasDTO;
import com.tiendayuliana.backend.exception.BadRequestException;
import com.tiendayuliana.backend.service.ReporteComprasService;
import com.tiendayuliana.backend.service.ReporteService;
import com.tiendayuliana.backend.repository.VentaDetalleRepository;
import com.tiendayuliana.backend.repository.ProductoRepository;


@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ReporteService reporteService;
    private final ReporteComprasService reporteComprasService;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final ProductoRepository productoRepository;

    public ReportesController(ReporteService reporteService, ReporteComprasService reporteComprasService,
                              VentaDetalleRepository ventaDetalleRepository, ProductoRepository productoRepository) {
        this.reporteService = reporteService;
        this.reporteComprasService = reporteComprasService;
        this.ventaDetalleRepository = ventaDetalleRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping("/ventas")
    public ReporteVentasDTO ventas(@RequestParam String periodo) {
        return reporteService.ventasPorPeriodo(periodo);
    }

    @GetMapping("/compras")
    public List<ReporteComprasDTO> compras(@RequestParam LocalDate desde,
                                           @RequestParam LocalDate hasta,
                                           @RequestParam(name = "proveedorId", required = false) Integer proveedorId) {
        if (hasta.isBefore(desde)) {
            throw new BadRequestException("La fecha 'hasta' no puede ser anterior a 'desde'");
        }
        return reporteComprasService.compras(desde, hasta, proveedorId);
    }

    @GetMapping("/utilidades")
    public java.util.Map<String, Object> utilidades(@RequestParam LocalDate desde,
                                                    @RequestParam LocalDate hasta) {
        var dets = ventaDetalleRepository.findAllBetween(desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay().minusNanos(1));
        java.math.BigDecimal utilidad = java.math.BigDecimal.ZERO;
        long cantidad = 0;
        for (var d : dets) {
            var costo = d.getProducto().getCostoActual() == null ? java.math.BigDecimal.ZERO : d.getProducto().getCostoActual();
            var linea = d.getPrecioUnitario().subtract(costo)
                    .multiply(java.math.BigDecimal.valueOf(d.getCantidad()))
                    .subtract(d.getDescuento() == null ? java.math.BigDecimal.ZERO : d.getDescuento());
            utilidad = utilidad.add(linea);
            cantidad += d.getCantidad();
        }
        return java.util.Map.of("utilidad", utilidad, "items", cantidad);
    }

    @GetMapping("/top-productos")
    public java.util.List<java.util.Map<String, Object>> topProductos(@RequestParam LocalDate desde,
                                                                      @RequestParam LocalDate hasta,
                                                                      @RequestParam(defaultValue = "10") int limit) {
        var rows = ventaDetalleRepository.topProductos(desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay().minusNanos(1));
        return rows.stream().limit(limit).map(r -> {
            Integer idProd = (Integer) r[0];
            Long cant = ((Number) r[1]).longValue();
            var prod = productoRepository.findById(idProd).orElse(null);
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("idProducto", idProd);
            m.put("nombre", prod == null ? null : prod.getNombre());
            m.put("cantidad", cant);
            return m;
        }).toList();
    }

    @GetMapping(value = "/ventas.pdf")
    public org.springframework.http.ResponseEntity<byte[]> ventasPdf(@RequestParam String periodo) throws Exception {
        var rep = ventas(periodo);
        var baos = new java.io.ByteArrayOutputStream();
        var doc = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(doc, baos);
        doc.open();
        doc.add(new com.lowagie.text.Paragraph("Reporte de Ventas - " + rep.periodo()));
        doc.add(new com.lowagie.text.Paragraph("Total: " + rep.totalVentas()));
        doc.close();
        return org.springframework.http.ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=ventas-"+periodo+".pdf")
                .body(baos.toByteArray());
    }

    @GetMapping(value = "/ventas.xlsx")
    public org.springframework.http.ResponseEntity<byte[]> ventasXlsx(@RequestParam String periodo) throws Exception {
        var rep = ventas(periodo);
        var wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        var sheet = wb.createSheet("Ventas");
        var row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("Periodo");
        row0.createCell(1).setCellValue(rep.periodo());
        row0.createCell(2).setCellValue("Total");
        row0.createCell(3).setCellValue(rep.totalVentas().doubleValue());
        var bos = new java.io.ByteArrayOutputStream();
        wb.write(bos); wb.close();
        return org.springframework.http.ResponseEntity.ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header("Content-Disposition", "attachment; filename=ventas-"+rep.periodo()+".xlsx")
                .body(bos.toByteArray());
    }
}
