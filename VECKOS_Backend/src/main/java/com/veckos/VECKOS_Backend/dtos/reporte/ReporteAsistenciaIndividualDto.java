package com.veckos.VECKOS_Backend.dtos.reporte;

import com.veckos.VECKOS_Backend.dtos.asistencia.AsistenciaInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para un reporte de asistencia individual
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteAsistenciaIndividualDto {

    private Long usuarioId;
    private String nombreCompleto;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer totalClasesProgramadas;
    private Long totalAsistencias;
    private Double porcentajeAsistencia;
    private List<AsistenciaInfoDto> asistencias;
}
