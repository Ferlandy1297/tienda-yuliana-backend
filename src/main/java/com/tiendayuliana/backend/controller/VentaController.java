package com.tiendayuliana.backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendayuliana.backend.dto.venta.PagoCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaCreateDTO;
import com.tiendayuliana.backend.dto.venta.VentaListItemDTO;
import com.tiendayuliana.backend.dto.venta.VentaResponseDTO;
import com.tiendayuliana.backend.service.VentaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crear(@Valid @RequestBody VentaCreateDTO dto) {
        VentaResponseDTO created = ventaService.crear(dto);
        return ResponseEntity.created(URI.create("/api/ventas/" + created.idVenta()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<VentaListItemDTO>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }

    @PostMapping("/{id}/pagos")
    public ResponseEntity<VentaResponseDTO> registrarPago(@PathVariable Integer id,
                                                          @Valid @RequestBody PagoCreateDTO dto) {
        return ResponseEntity.ok(ventaService.registrarPago(id, dto));
    }

    @GetMapping("/{id}/comprobante.pdf")
    public org.springframework.http.ResponseEntity<byte[]> comprobante(@PathVariable Integer id) throws Exception {
        // Generación simple de PDF con OpenPDF
        var venta = ventaService.getById(id);
        var baos = new java.io.ByteArrayOutputStream();
        var doc = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(doc, baos);
        doc.open();
        doc.add(new com.lowagie.text.Paragraph("Comprobante de Venta #" + venta.idVenta()));
        doc.add(new com.lowagie.text.Paragraph("Fecha: " + venta.fechaHora()));
        doc.add(new com.lowagie.text.Paragraph("Tipo: " + venta.tipo()));
        doc.add(new com.lowagie.text.Paragraph("Total: " + venta.total()));
        com.lowagie.text.Table table = new com.lowagie.text.Table(5);
        table.addCell("Producto"); table.addCell("Lote"); table.addCell("Cant"); table.addCell("Precio"); table.addCell("Desc");
        for (var d : venta.detalles()) {
            table.addCell(String.valueOf(d.idProducto()));
            table.addCell(String.valueOf(d.idLote()));
            table.addCell(String.valueOf(d.cantidad()));
            table.addCell(String.valueOf(d.precioUnitario()));
            table.addCell(String.valueOf(d.descuento()));
        }
        doc.add(table);
        doc.close();
        byte[] pdf = baos.toByteArray();
        return org.springframework.http.ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=comprobante-"+id+".pdf")
                .body(pdf);
    }
}
