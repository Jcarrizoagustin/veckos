package com.veckos.VECKOS_Backend.dtos.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO para un reporte de asistencia general
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteAsistenciaGeneralDto {

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer totalUsuarios;
    private Integer totalClasesProgramadas;
    private Integer totalAsistencias;
    private Double porcentajeAsistenciaPromedio;
    private List<Object[]> rankingUsuarios;
    private Map<LocalDateTime, Long> asistenciasPorDia;
}
