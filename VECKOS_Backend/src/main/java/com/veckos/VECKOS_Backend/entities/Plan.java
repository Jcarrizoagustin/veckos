package com.veckos.VECKOS_Backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column
    private String descripcion;

    @OneToMany(mappedBy = "plan",fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();
}
