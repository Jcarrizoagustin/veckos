package com.veckos.VECKOS_Backend.dtos.asistencia;

import com.veckos.VECKOS_Backend.entities.Asistencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO para mostrar información de una asistencia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaInfoDto {

    private Long id;
    private Long claseId;
    private LocalDate fechaClase;
    private LocalTime horaClase;
    private String nombreUsuario;
    private String apellidoUsuario;
    private Boolean presente;
    private LocalDateTime fechaRegistro;

    // Constructor para convertir desde entidad
    public AsistenciaInfoDto(Asistencia asistencia) {
        this.id = asistencia.getId();
        this.claseId = asistencia.getClase().getId();
        this.fechaClase = asistencia.getClase().getFecha();
        this.horaClase = asistencia.getClase().getTurno().getHora();
        this.nombreUsuario = asistencia.getUsuario().getNombre();
        this.apellidoUsuario = asistencia.getUsuario().getApellido();
        this.presente = asistencia.getPresente();
        this.fechaRegistro = asistencia.getFechaRegistro();
    }
}
