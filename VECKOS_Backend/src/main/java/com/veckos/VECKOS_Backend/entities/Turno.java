package com.veckos.VECKOS_Backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "turnos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dia_semana", "hora"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DayOfWeek diaSemana;

    @Column(nullable = false)
    private LocalTime hora;

    @Column
    private String descripcion;

    @OneToMany(mappedBy = "turno")
    private List<DetalleInscripcion> detallesInscripcion = new ArrayList<>();

    @OneToMany(mappedBy = "turno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Clase> clases = new ArrayList<>();

    // Métodos helper para relaciones bidireccionales
    public void addClass(Clase clase) {
        clases.add(clase);
        clase.setTurno(this);
    }

    public void removeClass(Clase clase) {
        clases.remove(clase);
        clase.setTurno(null);
    }
}
