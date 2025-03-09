package com.veckos.VECKOS_Backend.dtos.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * DTO para las estadísticas de un usuario en el dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class DashboardUsuarioStatsDto {

    private Long usuarioId;
    private String nombreCompleto;
    private String planActual;
    private LocalDate fechaFinPlan;
    private Integer diasRestantes;
    private Double porcentajeAsistencia;
    private Integer asistenciasMesActual;
    private Integer totalPagosRealizados;
    private BigDecimal totalPagado;
    private LocalDate ultimoPago;
    private Map<String, Integer> asistenciasPorDia;
}
