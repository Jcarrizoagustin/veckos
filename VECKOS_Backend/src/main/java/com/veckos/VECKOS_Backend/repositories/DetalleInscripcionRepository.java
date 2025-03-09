package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.DetalleInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface DetalleInscripcionRepository extends JpaRepository<DetalleInscripcion, Long> {

    List<DetalleInscripcion> findByInscripcionId(Long inscripcionId);

    List<DetalleInscripcion> findByTurnoId(Long turnoId);

    List<DetalleInscripcion> findByInscripcionIdAndDiaSemana(Long inscripcionId, DayOfWeek diaSemana);

    @Query("SELECT d FROM DetalleInscripcion d " +
            "JOIN d.inscripcion i " +
            "WHERE i.usuario.id = :usuarioId " +
            "AND i.fechaFin >= CURRENT_DATE " +
            "ORDER BY d.diaSemana ASC, d.turno.hora ASC")
    List<DetalleInscripcion> findDetallesByUsuarioIdActivos(@Param("usuarioId") Long usuarioId);

    @Query("SELECT d.diaSemana FROM DetalleInscripcion d " +
            "WHERE d.inscripcion.id = :inscripcionId " +
            "ORDER BY CASE d.diaSemana " +
            "    WHEN 'MONDAY' THEN 1 " +
            "    WHEN 'TUESDAY' THEN 2 " +
            "    WHEN 'WEDNESDAY' THEN 3 " +
            "    WHEN 'THURSDAY' THEN 4 " +
            "    WHEN 'FRIDAY' THEN 5 " +
            "    WHEN 'SATURDAY' THEN 6 " +
            "    WHEN 'SUNDAY' THEN 7 " +
            "END")
    List<DayOfWeek> findDiasSemanaByInscripcionIdOrdered(@Param("inscripcionId") Long inscripcionId);
}
