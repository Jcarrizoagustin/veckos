package com.veckos.VECKOS_Backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inscripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private Integer frecuencia;  // 3 o 5 días por semana

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPago estadoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_inscripcion", nullable = false)
    private EstadoInscripcion estadoInscripcion;

    @Column(name = "ultimo_pago")
    private LocalDate ultimoPago;

    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleInscripcion> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    // Enum para el estado del pago
    public enum EstadoPago {
        PAGA,              // Verde
        PENDIENTE// Azul
    }

    public enum EstadoInscripcion {// Verde
        EN_CURSO,            // Rojo
        COMPLETADA           // Azul
    }

    // Métodos helper para relaciones bidireccionales
    public void addDetalle(DetalleInscripcion detalle) {
        detalles.add(detalle);
        detalle.setInscripcion(this);
    }

    public void removeDetalle(DetalleInscripcion detalle) {
        detalles.remove(detalle);
        detalle.setInscripcion(null);
    }

    public void addPago(Pago pago) {
        pagos.add(pago);
        pago.setInscripcion(this);
    }

    public void removePago(Pago pago) {
        pagos.remove(pago);
        pago.setInscripcion(null);
    }
}
