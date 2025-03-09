package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    Optional<Turno> findByDiaSemanaAndHora(DayOfWeek diaSemana, LocalTime hora);

    List<Turno> findByDiaSemanaOrderByHoraAsc(DayOfWeek diaSemana);

    boolean existsByDiaSemanaAndHora(DayOfWeek diaSemana, LocalTime hora);

    @Query("SELECT t FROM Turno t " +
            "LEFT JOIN t.detallesInscripcion d " +
            "LEFT JOIN d.inscripcion i " +
            "LEFT JOIN i.usuario u " +
            "WHERE t.diaSemana = :diaSemana " +
            "GROUP BY t " +
            "ORDER BY t.hora ASC")
    List<Turno> findTurnosByDiaSemanaConUsuarios(@Param("diaSemana") DayOfWeek diaSemana);

    @Query("SELECT t FROM Turno t " +
            "LEFT JOIN t.detallesInscripcion d " +
            "GROUP BY t " +
            "ORDER BY COUNT(d) DESC")
    List<Turno> findAllOrderByOcupacion();
}