package com.veckos.VECKOS_Backend.dtos.clase;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para generar clases a partir de turnos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class GenerarClasesDto {

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    private List<Long> turnosIds; // Si es null, se generan para todos los turnos
}
