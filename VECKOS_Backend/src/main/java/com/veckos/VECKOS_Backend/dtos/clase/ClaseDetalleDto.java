package com.veckos.VECKOS_Backend.dtos.clase;

import com.veckos.VECKOS_Backend.dtos.asistencia.AsistenciaInfoDto;
import com.veckos.VECKOS_Backend.entities.Clase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO para mostrar información detallada de una clase con asistencias
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ClaseDetalleDto {

    private Long id;
    private Long turnoId;
    private DayOfWeek diaSemana;
    private LocalTime hora;
    private LocalDate fecha;
    private String descripcion;
    private List<AsistenciaInfoDto> asistencias;
    private Integer cantidadAsistencias;
    private Integer cantidadPresentes;
    private Double porcentajeAsistencia;

    // Constructor para convertir desde entidad y lista de asistencias
    public ClaseDetalleDto(Clase clase, List<AsistenciaInfoDto> asistencias) {
        this.id = clase.getId();
        this.turnoId = clase.getTurno().getId();
        this.diaSemana = clase.getTurno().getDiaSemana();
        this.hora = clase.getTurno().getHora();
        this.fecha = clase.getFecha();
        this.descripcion = clase.getDescripcion();
        this.asistencias = asistencias;
        this.cantidadAsistencias = asistencias.size();
        this.cantidadPresentes = (int) asistencias.stream()
                .filter(a -> a.getPresente())
                .count();

        if (this.cantidadAsistencias > 0) {
            this.porcentajeAsistencia = (this.cantidadPresentes * 100.0) / this.cantidadAsistencias;
        } else {
            this.porcentajeAsistencia = 0.0;
        }
    }
}
