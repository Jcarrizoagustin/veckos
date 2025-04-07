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

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    // Métodos helper para relaciones bidireccionales
    public void addInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
        inscripcion.setUsuario(this);
    }

    public void removeInscripcion(Inscripcion inscripcion) {
        inscripciones.remove(inscripcion);
        inscripcion.setUsuario(null);
    }

    /*
    * Metodo para obtener el estado del usuario. La logica es que un usuario esta activo si:
    * - Tiene una subscripcion activa: Es decir, tiene una subscripcion en curso con estado PAGA.
    * - Una subscripcion en curso seria una subscripcion con el dia actual entre dia inicio y dia fin.
    * - Hay que buscar una subscripcion, obtenerla y ver el estado de pago.
    * */
    public Inscripcion.EstadoPago obtenerEstado(){
        int size = this.inscripciones.size();
        LocalDate hoy = LocalDate.now();
        if(size > 0){
            //return this.inscripciones.get(size - 1).getEstadoPago();
            return this.inscripciones.stream()
                    .filter(inscripcion -> !hoy.isBefore(inscripcion.getFechaInicio()) && !hoy.isAfter(inscripcion.getFechaFin()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No existe inscripcion en curso"))
                    .getEstadoPago();
        }
        return Inscripcion.EstadoPago.PENDIENTE;
    }

    //Metodo para obtener una inscripcion activa, es decir EN CURSO
    public Inscripcion obtenerInscripcionActiva(){
        return this.inscripciones.stream().filter(inscripcion -> inscripcion.getEstadoInscripcion().equals(Inscripcion.EstadoInscripcion.EN_CURSO))
                .findFirst()
                .orElse(null);
    }
}
