package com.veckos.VECKOS_Backend.dtos.reporte;

import com.veckos.VECKOS_Backend.dtos.asistencia.AsistenciaInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para un reporte de asistencia individual
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ReporteAsistenciaIndividualDto {

    private Long usuarioId;
    private String nombreCompleto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer totalClasesProgramadas;
    private Integer totalAsistencias;
    private Double porcentajeAsistencia;
    private List<AsistenciaInfoDto> asistencias;
}
