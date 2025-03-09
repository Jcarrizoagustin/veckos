package com.veckos.VECKOS_Backend.dtos.inscripcion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionCrearDto {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID del plan es obligatorio")
    private Long planId;

    private LocalDate fechaInicio;

    @NotNull(message = "La frecuencia es obligatoria")
    @Min(value = 3, message = "La frecuencia debe ser 3 o 5 días")
    @Max(value = 5, message = "La frecuencia debe ser 3 o 5 días")
    private Integer frecuencia;

    @NotNull(message = "Los detalles de inscripción son obligatorios")
    private List<DetalleInscripcionDto> detalles;
}
