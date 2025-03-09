package com.veckos.VECKOS_Backend.dtos.asistencia;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para registrar múltiples asistencias en una clase
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class AsistenciasPorClaseDto {

    @NotNull(message = "El ID de la clase es obligatorio")
    private Long claseId;

    @NotNull(message = "La lista de IDs de usuarios presentes es obligatoria")
    private List<Long> usuariosPresentes;
}
