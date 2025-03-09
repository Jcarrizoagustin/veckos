package com.veckos.VECKOS_Backend.dtos.plan;

import com.veckos.VECKOS_Backend.entities.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para mostrar información básica de un plan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PlanInfoDto {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String descripcion;
    private Integer cantidadInscripciones;

    // Constructor para convertir desde entidad
    public PlanInfoDto(Plan plan) {
        this.id = plan.getId();
        this.nombre = plan.getNombre();
        this.precio = plan.getPrecio();
        this.descripcion = plan.getDescripcion();
        this.cantidadInscripciones = plan.getInscripciones().size();
    }
}
