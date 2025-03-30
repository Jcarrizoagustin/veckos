package com.veckos.VECKOS_Backend.controllers;

import com.veckos.VECKOS_Backend.dtos.clase.ClaseDto;
import com.veckos.VECKOS_Backend.dtos.clase.ClaseInfoDto;
import com.veckos.VECKOS_Backend.entities.Clase;
import com.veckos.VECKOS_Backend.services.ClaseService;
import com.veckos.VECKOS_Backend.services.TurnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clases")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @Autowired
    private TurnoService turnoService;

    @GetMapping
    public ResponseEntity<List<ClaseInfoDto>> getAllClases() {
        List<Clase> clases = claseService.findAll();
        List<ClaseInfoDto> clasesDto = clases.stream()
                .map(ClaseInfoDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clasesDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaseInfoDto> getClaseById(@PathVariable Long id) {
        Clase clase = claseService.findById(id);
        ClaseInfoDto response = new ClaseInfoDto(clase);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/turno/{turnoId}")
    public ResponseEntity<List<ClaseInfoDto>> getClasesByTurnoId(@PathVariable Long turnoId) {
        List<Clase> clases = claseService.findByTurnoId(turnoId);
        List<ClaseInfoDto> clasesDto = clases.stream()
                .map(ClaseInfoDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clasesDto);
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<ClaseInfoDto>> getClasesByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Clase> clases = claseService.findByFechaOrderByHora(fecha);
        List<ClaseInfoDto> clasesDto = clases.stream()
                .map(ClaseInfoDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clasesDto);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<ClaseDto>> getClasesByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Clase> clases = claseService.findByFechaBetween(fechaInicio, fechaFin);
        List<ClaseDto> clasesDto = clases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clasesDto);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ClaseDto>> getClasesWithAsistenciaByUsuarioId(
            @PathVariable Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Clase> clases = claseService.findClasesWithAsistenciaByUsuarioIdAndFechaBetween(
                usuarioId, fechaInicio, fechaFin);
        List<ClaseDto> clasesDto = clases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clasesDto);
    }

    @PostMapping
    public ResponseEntity<?> createClase(@Valid @RequestBody ClaseDto claseDto) {
        try {
            Clase clase = convertToEntity(claseDto);
            Clase savedClase = claseService.save(clase);
            return new ResponseEntity<>(convertToDto(savedClase), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaseDto> updateClase(
            @PathVariable Long id,
            @Valid @RequestBody ClaseDto claseDto) {

        Clase clase = convertToEntity(claseDto);
        Clase updatedClase = claseService.update(id, clase);
        return ResponseEntity.ok(convertToDto(updatedClase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClase(@PathVariable Long id) {
        try {
            claseService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/estadisticas/periodo")
    public ResponseEntity<Long> getEstadisticasAsistenciasPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long cantidadAsistencias = claseService.countAsistenciasTotalesEnPeriodo(fechaInicio, fechaFin);
        return ResponseEntity.ok(cantidadAsistencias);
    }

    /**
     * Convierte una entidad Clase a su DTO correspondiente
     */
    private ClaseDto convertToDto(Clase clase) {
        return new ClaseDto(clase);
    }

    /**
     * Convierte un DTO de Clase a su entidad correspondiente
     */
    private Clase convertToEntity(ClaseDto claseDto) {
        Clase clase = new Clase();
        clase.setId(claseDto.getId());
        clase.setTurno(turnoService.findById(claseDto.getTurnoId()));
        clase.setFecha(claseDto.getFecha());
        clase.setDescripcion(claseDto.getDescripcion());
        return clase;
    }
}
