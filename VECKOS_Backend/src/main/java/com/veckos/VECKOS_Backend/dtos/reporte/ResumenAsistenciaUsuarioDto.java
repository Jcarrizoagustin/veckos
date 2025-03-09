package com.veckos.VECKOS_Backend.dtos.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el resumen de asistencia por usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ResumenAsistenciaUsuarioDto {

    private Long usuarioId;
    private String nombreCompleto;
    private Integer clasesProgramadas;
    private Integer asistencias;
    private Double porcentajeAsistencia;
}
