package com.veckos.VECKOS_Backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(unique = true, nullable = false)
    private String dni;

    @Column
    private String cuil;

    @Column
    private String telefono;

    @Column
    private String correo;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoUsuario estado;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    // Enum para el estado del usuario
    public enum EstadoUsuario {
        ACTIVO,
        INACTIVO,
        PENDIENTE
    }

    // Métodos helper para relaciones bidireccionales
    public void addInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
        inscripcion.setUsuario(this);
    }

    public void removeInscripcion(Inscripcion inscripcion) {
        inscripciones.remove(inscripcion);
        inscripcion.setUsuario(null);
    }
}
