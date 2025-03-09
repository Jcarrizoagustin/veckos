package com.veckos.VECKOS_Backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clases", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"turno_id", "fecha"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column
    private String descripcion;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistencia> asistencias = new ArrayList<>();

    // Métodos helper para relaciones bidireccionales
    public void addAsistencia(Asistencia asistencia) {
        asistencias.add(asistencia);
        asistencia.setClase(this);
    }

    public void removeAsistencia(Asistencia asistencia) {
        asistencias.remove(asistencia);
        asistencia.setClase(null);
    }
}
