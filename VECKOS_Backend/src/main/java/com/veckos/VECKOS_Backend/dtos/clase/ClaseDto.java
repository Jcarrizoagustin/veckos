package com.veckos.VECKOS_Backend.dtos.clase;

import com.veckos.VECKOS_Backend.entities.Clase;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para crear o actualizar una clase
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDto {

    private Long id;

    @NotNull(message = "El ID del turno es obligatorio")
    private Long turnoId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String descripcion;

    // Constructor para convertir desde entidad
    public ClaseDto(Clase clase) {
        this.id = clase.getId();
        this.turnoId = clase.getTurno().getId();
        this.fecha = clase.getFecha();
        this.descripcion = clase.getDescripcion();
    }
}