package com.veckos.VECKOS_Backend.dtos.inscripcion;

import com.veckos.VECKOS_Backend.entities.Inscripcion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionInfoDto {

    private Long id;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String nombrePlan;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer frecuencia;
    private Inscripcion.EstadoPago estadoPago;
    private LocalDate ultimoPago;
    private List<DetalleInscripcionInfoDto> detalles;

    // Constructor para convertir desde entidad
    public InscripcionInfoDto(Inscripcion inscripcion) {
        this.id = inscripcion.getId();
        this.nombreUsuario = inscripcion.getUsuario().getNombre();
        this.apellidoUsuario = inscripcion.getUsuario().getApellido();
        this.nombrePlan = inscripcion.getPlan().getNombre();
        this.fechaInicio = inscripcion.getFechaInicio();
        this.fechaFin = inscripcion.getFechaFin();
        this.frecuencia = inscripcion.getFrecuencia();
        this.estadoPago = inscripcion.getEstadoPago();
        this.ultimoPago = inscripcion.getUltimoPago();

        if (inscripcion.getDetalles() != null) {
            this.detalles = inscripcion.getDetalles().stream()
                    .map(DetalleInscripcionInfoDto::new)
                    .collect(Collectors.toList());
        }
    }
}
