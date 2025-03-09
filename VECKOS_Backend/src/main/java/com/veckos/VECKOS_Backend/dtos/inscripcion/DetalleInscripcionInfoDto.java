package com.veckos.VECKOS_Backend.dtos.inscripcion;

import com.veckos.VECKOS_Backend.entities.DetalleInscripcion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
class DetalleInscripcionInfoDto {

    private Long id;
    private DayOfWeek diaSemana;
    private Long turnoId;
    private LocalTime horaTurno;

    // Constructor para convertir desde entidad
    public DetalleInscripcionInfoDto(DetalleInscripcion detalle) {
        this.id = detalle.getId();
        this.diaSemana = detalle.getDiaSemana();
        this.turnoId = detalle.getTurno().getId();
        this.horaTurno = detalle.getTurno().getHora();
    }
}
