package com.veckos.VECKOS_Backend.dtos.inscripcion;

import com.veckos.VECKOS_Backend.entities.Inscripcion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionInfoDto {

    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String nombrePlan;
    private Long planId;
    private BigDecimal precioPlan;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer frecuencia;
    private Inscripcion.EstadoPago estadoPago;
    private LocalDateTime ultimoPago;
    private List<DetalleInscripcionInfoDto> detalles;
    private Inscripcion.EstadoInscripcion estadoInscripcion;

    // Constructor para convertir desde entidad
    public InscripcionInfoDto(Inscripcion inscripcion) {
        this.id = inscripcion.getId();
        this.usuarioId = inscripcion.getUsuario().getId();
        this.nombreUsuario = inscripcion.getUsuario().getNombre();
        this.apellidoUsuario = inscripcion.getUsuario().getApellido();
        this.nombrePlan = inscripcion.getPlan().getNombre();
        this.planId = inscripcion.getPlan().getId();
        this.fechaInicio = inscripcion.getFechaInicio().atStartOfDay();
        this.fechaFin = inscripcion.getFechaFin().atStartOfDay();
        this.frecuencia = inscripcion.getFrecuencia();
        this.estadoPago = inscripcion.getEstadoPago();
        //this.ultimoPago = inscripcion.getUltimoPago().atStartOfDay();
        this.precioPlan = inscripcion.getPlan().getPrecio();
        this.estadoInscripcion = inscripcion.getEstadoInscripcion();

        if(Objects.nonNull(inscripcion.getUltimoPago())){
            this.ultimoPago = inscripcion.getUltimoPago().atStartOfDay();
        }

        if (inscripcion.getDetalles() != null) {
            this.detalles = inscripcion.getDetalles().stream()
                    .map(DetalleInscripcionInfoDto::new)
                    .collect(Collectors.toList());
        }
    }
}
