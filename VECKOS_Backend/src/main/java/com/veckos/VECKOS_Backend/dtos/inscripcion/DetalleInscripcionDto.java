package com.veckos.VECKOS_Backend.dtos.inscripcion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleInscripcionDto {

    @NotNull(message = "El ID del turno es obligatorio")
    private Long turnoId;

    @NotNull(message = "El día de la semana es obligatorio")
    private DayOfWeek diaSemana;
}
