package com.veckos.VECKOS_Backend.controllers;

import com.veckos.VECKOS_Backend.dtos.asistencia.AsistenciaRegistrarDto;
import com.veckos.VECKOS_Backend.entities.Asistencia;
import com.veckos.VECKOS_Backend.services.AsistenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Asistencia>> getAllAsistencias() {
        List<Asistencia> asistencias = asistenciaService.findAll();
        return ResponseEntity.ok(asistencias);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<Asistencia> getAsistenciaById(@PathVariable Long id) {
        Asistencia asistencia = asistenciaService.findById(id);
        return ResponseEntity.ok(asistencia);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Asistencia>> getAsistenciasByUsuarioId(@PathVariable Long usuarioId) {
        List<Asistencia> asistencias = asistenciaService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(asistencias);
    }

    @GetMapping("/clase/{claseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Asistencia>> getAsistenciasByClaseId(@PathVariable Long claseId) {
        List<Asistencia> asistencias = asistenciaService.findByClaseId(claseId);
        return ResponseEntity.ok(asistencias);
    }

    @GetMapping("/usuario/{usuarioId}/fecha")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Asistencia>> getAsistenciasByUsuarioIdAndFecha(
            @PathVariable Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Asistencia> asistencias = asistenciaService.findByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(asistencias);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<Asistencia> registrarAsistencia(@Valid @RequestBody AsistenciaRegistrarDto asistenciaDto) {
        Asistencia asistencia = asistenciaService.registrarAsistencia(
                asistenciaDto.getClaseId(),
                asistenciaDto.getUsuarioId(),
                asistenciaDto.getPresente());
        return new ResponseEntity<>(asistencia, HttpStatus.CREATED);
    }

    @PostMapping("/clase/{claseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Asistencia>> registrarAsistenciasPorClase(
            @PathVariable Long claseId,
            @RequestBody List<Long> usuariosPresentes) {
        List<Asistencia> asistencias = asistenciaService.registrarAsistenciasPorClase(claseId, usuariosPresentes);
        return ResponseEntity.ok(asistencias);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAsistencia(@PathVariable Long id) {
        asistenciaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estadisticas/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<Long> getEstadisticasAsistenciaUsuario(
            @PathVariable Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long cantidadAsistencias = asistenciaService.countAsistenciasByUsuarioIdAndFechaBetween(
                usuarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(cantidadAsistencias);
    }

    @GetMapping("/ranking")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getRankingUsuariosPorAsistencia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Object[]> ranking = asistenciaService.findUsuariosConMayorAsistenciaEnPeriodo(fechaInicio, fechaFin);
        return ResponseEntity.ok(ranking);
    }
}
