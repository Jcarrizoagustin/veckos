package com.veckos.VECKOS_Backend.dtos.clase;

import com.veckos.VECKOS_Backend.entities.Clase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO para mostrar información de una clase
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaseInfoDto {

    private Long id;
    private Long turnoId;
    private DayOfWeek diaSemana;
    private LocalTime hora;
    private LocalDate fecha;
    private String descripcion;
    private Integer cantidadAsistencias;
    private Integer cantidadPresentes;

    // Constructor para convertir desde entidad
    public ClaseInfoDto(Clase clase) {
        this.id = clase.getId();
        this.turnoId = clase.getTurno().getId();
        this.diaSemana = clase.getTurno().getDiaSemana();
        this.hora = clase.getTurno().getHora();
        this.fecha = clase.getFecha();
        this.descripcion = clase.getDescripcion();

        if (clase.getAsistencias() != null) {
            this.cantidadAsistencias = clase.getTurno().getDetallesInscripcion().size();
            this.cantidadPresentes = (int) clase.getAsistencias().stream()
                    .filter(a -> a.getPresente())
                    .count();
        } else {
            this.cantidadAsistencias = 0;
            this.cantidadPresentes = 0;
        }
    }
}
