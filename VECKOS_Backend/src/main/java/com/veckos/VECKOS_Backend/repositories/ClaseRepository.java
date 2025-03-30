package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    List<Clase> findByTurnoId(Long turnoId);

    List<Clase> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    Optional<Clase> findByTurnoIdAndFecha(Long turnoId, LocalDate fecha);

    @Query("SELECT c FROM Clase c " +
            "JOIN c.turno t " +
            //"WHERE t.diaSemana = FUNCTION('dayofweek', :fecha) " +
            "WHERE c.fecha = :fecha " +
            "ORDER BY t.hora ASC")
    List<Clase> findByFechaOrderByHora(@Param("fecha") LocalDate fecha);

    @Query("SELECT c FROM Clase c " +
            "JOIN c.asistencias a " +
            "JOIN a.usuario u " +
            "WHERE u.id = :usuarioId " +
            "AND c.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY c.fecha ASC, c.turno.hora ASC")
    List<Clase> findClasesWithAsistenciaByUsuarioIdAndFechaBetween(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT COUNT(c) FROM Clase c " +
            "JOIN c.asistencias a " +
            "WHERE a.presente = true " +
            "AND c.fecha BETWEEN :fechaInicio AND :fechaFin")
    Long countAsistenciasTotalesEnPeriodo(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}
