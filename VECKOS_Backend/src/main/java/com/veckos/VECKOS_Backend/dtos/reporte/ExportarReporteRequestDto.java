package com.veckos.VECKOS_Backend.dtos.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * DTO para solicitar la exportación de un reporte a PDF
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ExportarReporteRequestDto {

    private String tipoReporte; // "asistencia", "financiero", "inscripciones"
    private Long usuarioId; // Para reportes individuales
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String formato; // "pdf", "excel", "csv"
    private Map<String, Boolean> opciones; // Opciones específicas para el reporte
}
