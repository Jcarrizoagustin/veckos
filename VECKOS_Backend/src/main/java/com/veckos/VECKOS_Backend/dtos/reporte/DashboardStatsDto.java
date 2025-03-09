package com.veckos.VECKOS_Backend.dtos.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO para dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class DashboardStatsDto {

    private Integer usuariosActivos;
    private Integer usuariosInactivos;
    private Integer inscripcionesActivas;
    private Integer inscripcionesProximasAVencer;
    private BigDecimal ingresosMesActual;
    private BigDecimal ingresosMesAnterior;
    private Double porcentajeCrecimientoIngresos;
    private Map<String, Integer> distribucionPorPlan;
    private Integer asistenciasHoy;
    private Double porcentajeAsistenciaPromedio;
    private Map<String, Integer> asistenciasUltimaSemana;
}
