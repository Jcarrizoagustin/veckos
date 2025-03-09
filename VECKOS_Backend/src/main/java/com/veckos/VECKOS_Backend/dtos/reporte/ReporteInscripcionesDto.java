package com.veckos.VECKOS_Backend.dtos.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * DTO para un reporte de inscripciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ReporteInscripcionesDto {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer totalInscripciones;
    private Integer inscripcionesNuevas;
    private Integer inscripcionesRenovadas;
    private Map<String, Integer> inscripcionesPorPlan;
    private Map<String, Integer> inscripcionesPorFrecuencia;
}
