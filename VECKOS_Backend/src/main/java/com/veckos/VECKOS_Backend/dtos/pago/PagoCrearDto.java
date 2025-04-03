package com.veckos.VECKOS_Backend.dtos.pago;

import com.veckos.VECKOS_Backend.entities.Pago;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear un pago
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoCrearDto {

    @NotNull(message = "El ID de la inscripción es obligatorio")
    private Long inscripcionId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser un valor positivo")
    private BigDecimal monto;

    private LocalDate fechaPago;

    //@NotNull(message = "El método de pago es obligatorio")
    private Pago.MetodoPago metodoPago;

    private Long cuentaId;

    private String descripcion;
}
