package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.dtos.pago.PagoInfoDto;
import com.veckos.VECKOS_Backend.dtos.reporte.ReporteFinancieroDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class ExcelExportService {

    public byte[] exportPagosToExcel(ReporteFinancieroDto reporte) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pagos");

            // Crear estilo para el encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Crear fila de encabezado
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Nombre Cliente", "Apellido Cliente", "DNI" ,"CUIL","Fecha de Pago", "Método de Pago", "Monto", "CBU Cuenta","Descripcion Cuenta"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar los datos
            int rowNum = 1;
            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
            for (PagoInfoDto pago : reporte.getPagos()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pago.getNombreUsuario());
                row.createCell(1).setCellValue(pago.getApellidoUsuario());
                row.createCell(2).setCellValue(pago.getDniUsuario());
                row.createCell(3).setCellValue(pago.getCuilUsuario());
                row.createCell(4).setCellValue(pago.getFechaPago().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                row.createCell(5).setCellValue(pago.getMetodoPago().toString());
                row.createCell(6).setCellValue(formatoMoneda.format(pago.getMonto()));
                row.createCell(7).setCellValue(pago.getCbuCuenta());
                row.createCell(8).setCellValue(pago.getDescripcionCuenta());
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escribir a un ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}