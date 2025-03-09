package com.veckos.VECKOS_Backend.controllers;

import com.veckos.VECKOS_Backend.dtos.inscripcion.InscripcionCrearDto;
import com.veckos.VECKOS_Backend.entities.DetalleInscripcion;
import com.veckos.VECKOS_Backend.entities.Inscripcion;
import com.veckos.VECKOS_Backend.entities.Plan;
import com.veckos.VECKOS_Backend.entities.Usuario;
import com.veckos.VECKOS_Backend.services.InscripcionService;
import com.veckos.VECKOS_Backend.services.PlanService;
import com.veckos.VECKOS_Backend.services.TurnoService;
import com.veckos.VECKOS_Backend.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PlanService planService;

    @Autowired
    private TurnoService turnoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Inscripcion>> getAllInscripciones() {
        List<Inscripcion> inscripciones = inscripcionService.findAll();
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<Inscripcion> getInscripcionById(@PathVariable Long id) {
        Inscripcion inscripcion = inscripcionService.findById(id);
        return ResponseEntity.ok(inscripcion);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Inscripcion>> getInscripcionesByUsuarioId(@PathVariable Long usuarioId) {
        List<Inscripcion> inscripciones = inscripcionService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/usuario/{usuarioId}/activa")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<?> getInscripcionActivaByUsuarioId(@PathVariable Long usuarioId) {
        return inscripcionService.findInscripcionActivaByUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/por-estado/{estado}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Inscripcion>> getInscripcionesByEstado(
            @PathVariable Inscripcion.EstadoPago estado) {
        List<Inscripcion> inscripciones = inscripcionService.findByEstadoPago(estado);
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/por-vencimiento")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Inscripcion>> getInscripcionesPorVencimiento(
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin) {
        List<Inscripcion> inscripciones = inscripcionService.findByFechaFinBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(inscripciones);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createInscripcion(@Valid @RequestBody InscripcionCrearDto inscripcionDto) {
        try {
            // Convertir DTO a entidad
            Inscripcion inscripcion = new Inscripcion();

            // Establecer usuario y plan
            Usuario usuario = usuarioService.findById(inscripcionDto.getUsuarioId());
            Plan plan = planService.findById(inscripcionDto.getPlanId());
            inscripcion.setUsuario(usuario);
            inscripcion.setPlan(plan);

            // Establecer fechas
            if (inscripcionDto.getFechaInicio() != null) {
                inscripcion.setFechaInicio(inscripcionDto.getFechaInicio());
            } else {
                inscripcion.setFechaInicio(LocalDate.now());
            }
            inscripcion.setFechaFin(inscripcion.getFechaInicio().plusMonths(1));

            // Establecer frecuencia
            inscripcion.setFrecuencia(inscripcionDto.getFrecuencia());

            // Establecer estado de pago inicial
            inscripcion.setEstadoPago(Inscripcion.EstadoPago.ACTIVO);

            // Crear detalles de inscripción
            Set<DetalleInscripcion> detalles = new HashSet<>();
            inscripcionDto.getDetalles().forEach(detalleDto -> {
                DetalleInscripcion detalle = new DetalleInscripcion();
                detalle.setTurno(turnoService.findById(detalleDto.getTurnoId()));
                detalle.setDiaSemana(detalleDto.getDiaSemana());
                detalles.add(detalle);
            });

            // Guardar inscripción con detalles
            Inscripcion savedInscripcion = inscripcionService.save(inscripcion, detalles);
            return new ResponseEntity<>(savedInscripcion, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateInscripcion(@PathVariable Long id, @RequestBody Inscripcion inscripcionDetails) {
        try {
            Inscripcion updatedInscripcion = inscripcionService.update(id, inscripcionDetails);
            return ResponseEntity.ok(updatedInscripcion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/renovar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> renovarInscripcion(@PathVariable Long id) {
        try {
            Inscripcion inscripcionRenovada = inscripcionService.renovarInscripcion(id);
            return ResponseEntity.ok(inscripcionRenovada);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/renovar-con-cambios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> renovarInscripcionConCambios(
            @PathVariable Long id,
            @RequestBody InscripcionCrearDto inscripcionDto) {
        try {
            // Convertir DTO a entidades
            Inscripcion nuevaInscripcion = new Inscripcion();

            // Recuperar la inscripción anterior para el usuario
            Inscripcion inscripcionAnterior = inscripcionService.findById(id);

            // Establecer usuario (mismo que la inscripción anterior)
            nuevaInscripcion.setUsuario(inscripcionAnterior.getUsuario());

            // Establecer plan (puede ser diferente)
            Long planId = inscripcionDto.getPlanId();
            Plan plan = planService.findById(planId);
            nuevaInscripcion.setPlan(plan);

            // Establecer fechas
            nuevaInscripcion.setFechaInicio(inscripcionAnterior.getFechaFin().plusDays(1));
            nuevaInscripcion.setFechaFin(nuevaInscripcion.getFechaInicio().plusMonths(1));

            // Establecer frecuencia (puede ser diferente)
            nuevaInscripcion.setFrecuencia(inscripcionDto.getFrecuencia());

            // Establecer estado de pago inicial
            nuevaInscripcion.setEstadoPago(Inscripcion.EstadoPago.ACTIVO);

            // Crear detalles de inscripción
            Set<DetalleInscripcion> detalles = new HashSet<>();
            inscripcionDto.getDetalles().forEach(detalleDto -> {
                DetalleInscripcion detalle = new DetalleInscripcion();
                detalle.setTurno(turnoService.findById(detalleDto.getTurnoId()));
                detalle.setDiaSemana(detalleDto.getDiaSemana());
                detalles.add(detalle);
            });

            // Guardar la nueva inscripción con detalles
            Inscripcion inscripcionRenovada = inscripcionService.renovarInscripcionConCambios(id, nuevaInscripcion, detalles);
            return ResponseEntity.ok(inscripcionRenovada);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInscripcion(@PathVariable Long id) {
        inscripcionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizar-estados")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<Void> actualizarEstadosPagos() {
        inscripcionService.actualizarEstadosPagos();
        return ResponseEntity.ok().build();
    }
}
