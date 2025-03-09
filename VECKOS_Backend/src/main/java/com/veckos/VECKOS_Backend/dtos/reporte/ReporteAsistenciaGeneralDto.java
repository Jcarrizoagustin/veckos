package com.veckos.VECKOS_Backend.dtos.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO para un reporte de asistencia general
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ReporteAsistenciaGeneralDto {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer totalUsuarios;
    private Integer totalClasesProgramadas;
    private Integer totalAsistencias;
    private Double porcentajeAsistenciaPromedio;
    private List<ResumenAsistenciaUsuarioDto> resumenPorUsuario;
    private Map<LocalDate, Integer> asistenciasPorDia;
}
