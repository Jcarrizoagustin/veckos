package com.veckos.VECKOS_Backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"clase_id", "usuario_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clase_id", nullable = false)
    private Clase clase;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Boolean presente;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
}
