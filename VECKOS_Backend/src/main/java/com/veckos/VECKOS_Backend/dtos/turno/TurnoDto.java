package com.veckos.VECKOS_Backend.dtos.turno;

import com.veckos.VECKOS_Backend.entities.Turno;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO para crear o actualizar un turno
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoDto {

    private Long id;

    @NotNull(message = "El día de la semana es obligatorio")
    private DayOfWeek diaSemana;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    private String descripcion;

    // Constructor para convertir desde entidad
    public TurnoDto(Turno turno) {
        this.id = turno.getId();
        this.diaSemana = turno.getDiaSemana();
        this.hora = turno.getHora();
        this.descripcion = turno.getDescripcion();
    }
}
