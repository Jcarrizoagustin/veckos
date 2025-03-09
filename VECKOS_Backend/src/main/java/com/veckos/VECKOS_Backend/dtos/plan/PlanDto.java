package com.veckos.VECKOS_Backend.dtos.plan;

import com.veckos.VECKOS_Backend.entities.Plan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para crear o actualizar un plan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDto {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un valor positivo")
    private BigDecimal precio;

    private String descripcion;

    // Constructor para convertir desde entidad
    public PlanDto(Plan plan) {
        this.id = plan.getId();
        this.nombre = plan.getNombre();
        this.precio = plan.getPrecio();
        this.descripcion = plan.getDescripcion();
    }
}

