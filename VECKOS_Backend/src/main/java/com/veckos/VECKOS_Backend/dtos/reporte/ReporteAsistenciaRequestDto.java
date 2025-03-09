package com.veckos.VECKOS_Backend.dtos.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para solicitar un reporte de asistencia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteAsistenciaRequestDto {

    private Long usuarioId; // Si es null, se generará para todos los usuarios
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean agruparPorDia = false;
    private Boolean agruparPorUsuario = false;
    private Boolean incluirSoloPresentes = false;
}
