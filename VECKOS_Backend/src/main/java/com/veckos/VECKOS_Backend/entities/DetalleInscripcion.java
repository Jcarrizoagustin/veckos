package com.veckos.VECKOS_Backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Entity
@Table(name = "detalles_inscripcion", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"inscripcion_id", "dia_semana"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleInscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;

    @ManyToOne
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DayOfWeek diaSemana;
}
