package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.Plan;
import com.veckos.VECKOS_Backend.repositories.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Transactional(readOnly = true)
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Plan findById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan no encontrado con ID: " + id));
    }

    @Transactional
    public Plan save(Plan plan) {
        return planRepository.save(plan);
    }

    @Transactional
    public Plan update(Long id, Plan planDetails) {
        Plan plan = findById(id);

        plan.setNombre(planDetails.getNombre());
        plan.setPrecio(planDetails.getPrecio());
        plan.setDescripcion(planDetails.getDescripcion());

        return planRepository.save(plan);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!planRepository.existsById(id)) {
            throw new EntityNotFoundException("Plan no encontrado con ID: " + id);
        }

        // Verificar si el plan tiene inscripciones antes de eliminar
        Plan plan = findById(id);
        if (!plan.getInscripciones().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un plan con inscripciones activas");
        }

        planRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Plan> findAllOrderByPrecioAsc() {
        return planRepository.findAllOrderByPrecioAsc();
    }

    @Transactional(readOnly = true)
    public List<Plan> findAllOrderByPopularidad() {
        return planRepository.findAllOrderByPopularidad();
    }

    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return planRepository.existsByNombre(nombre);
    }
}