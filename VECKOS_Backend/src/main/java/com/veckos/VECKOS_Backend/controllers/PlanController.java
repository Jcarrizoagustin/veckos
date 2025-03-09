package com.veckos.VECKOS_Backend.controllers;

import com.veckos.VECKOS_Backend.dtos.plan.PlanDto;
import com.veckos.VECKOS_Backend.entities.Plan;
import com.veckos.VECKOS_Backend.services.PlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/planes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<PlanDto>> getAllPlanes() {
        List<Plan> planes = planService.findAll();
        List<PlanDto> planesDto = planes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(planesDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<PlanDto> getPlanById(@PathVariable Long id) {
        Plan plan = planService.findById(id);
        return ResponseEntity.ok(convertToDto(plan));
    }

    @GetMapping("/por-precio")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<PlanDto>> getPlanesByPrecio() {
        List<Plan> planes = planService.findAllOrderByPrecioAsc();
        List<PlanDto> planesDto = planes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(planesDto);
    }

    @GetMapping("/por-popularidad")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<PlanDto>> getPlanesByPopularidad() {
        List<Plan> planes = planService.findAllOrderByPopularidad();
        List<PlanDto> planesDto = planes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(planesDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanDto> createPlan(@Valid @RequestBody PlanDto planDto) {
        // Verificar si ya existe un plan con el mismo nombre
        if (planService.existsByNombre(planDto.getNombre())) {
            return ResponseEntity.badRequest().build();
        }

        Plan plan = convertToEntity(planDto);
        Plan savedPlan = planService.save(plan);
        return new ResponseEntity<>(convertToDto(savedPlan), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanDto> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody PlanDto planDto) {

        Plan plan = convertToEntity(planDto);
        Plan updatedPlan = planService.update(id, plan);
        return ResponseEntity.ok(convertToDto(updatedPlan));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {
        try {
            planService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            // Capturar la excepción si el plan tiene inscripciones activas
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    /**
     * Convierte una entidad Plan a su DTO correspondiente
     */
    private PlanDto convertToDto(Plan plan) {
        return new PlanDto(plan);
    }

    /**
     * Convierte un DTO de Plan a su entidad correspondiente
     */
    private Plan convertToEntity(PlanDto planDto) {
        Plan plan = new Plan();
        plan.setId(planDto.getId());
        plan.setNombre(planDto.getNombre());
        plan.setPrecio(planDto.getPrecio());
        plan.setDescripcion(planDto.getDescripcion());
        return plan;
    }
}
