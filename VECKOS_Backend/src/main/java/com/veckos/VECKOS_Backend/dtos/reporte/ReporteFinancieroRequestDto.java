package com.veckos.VECKOS_Backend.dtos.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para solicitar un reporte financiero
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ReporteFinancieroRequestDto {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean agruparPorMes = false;
    private Boolean agruparPorMetodoPago = false;
    private Boolean agruparPorPlan = false;
}
