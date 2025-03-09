package com.veckos.VECKOS_Backend.dtos.asistencia;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaRegistrarDto {

    @NotNull(message = "El ID de la clase es obligatorio")
    private Long claseId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El estado de presencia es obligatorio")
    private Boolean presente;
}
