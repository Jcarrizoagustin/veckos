package com.veckos.VECKOS_Backend.dtos.pago;

import com.veckos.VECKOS_Backend.entities.Pago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para resumen de pagos por método
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ResumenPagoPorMetodoDto {

    private Pago.MetodoPago metodoPago;
    private Integer cantidad;
    private BigDecimal montoTotal;
}
