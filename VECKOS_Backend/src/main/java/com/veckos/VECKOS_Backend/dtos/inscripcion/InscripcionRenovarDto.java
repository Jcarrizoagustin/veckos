package com.veckos.VECKOS_Backend.dtos.inscripcion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class InscripcionRenovarDto {

    @NotNull(message = "El ID de la inscripción a renovar es obligatorio")
    private Long inscripcionId;

    private Long planId; // Opcional, si se quiere cambiar el plan

    private Integer frecuencia; // Opcional, si se quiere cambiar la frecuencia

    private List<DetalleInscripcionDto> detalles; // Opcional, si se quieren cambiar los detalles

    private Boolean mantenerMismosDetalles;
}
