package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    @Query("SELECT p FROM Plan p ORDER BY p.precio ASC")
    List<Plan> findAllOrderByPrecioAsc();

    @Query("SELECT p FROM Plan p JOIN p.inscripciones i GROUP BY p ORDER BY COUNT(i) DESC")
    List<Plan> findAllOrderByPopularidad();
}
