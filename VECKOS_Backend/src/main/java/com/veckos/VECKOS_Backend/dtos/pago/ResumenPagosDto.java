package com.veckos.VECKOS_Backend.dtos.pago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para resumen de pagos por período
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ResumenPagosDto {

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal montoTotal;
    private Integer cantidadPagos;
    private List<ResumenPagoPorMetodoDto> pagosPorMetodo;
}
