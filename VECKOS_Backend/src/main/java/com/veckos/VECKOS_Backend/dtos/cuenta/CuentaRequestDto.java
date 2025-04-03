package com.veckos.VECKOS_Backend.dtos.cuenta;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaRequestDto {

    @NotNull(message = "El cbu es obligatorio")
    private String cbu;
    @NotNull(message = "La descripcion es obligatoria")
    private String descripcion;
}
