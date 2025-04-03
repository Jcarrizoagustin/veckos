package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByUsuarioIdOrderByFechaRegistroDesc(Long usuarioId);

    List<Asistencia> findByClaseId(Long claseId);

    Optional<Asistencia> findByClaseIdAndUsuarioId(Long claseId, Long usuarioId);

    @Query("SELECT a FROM Asistencia a " +
            "JOIN a.clase c " +
            "WHERE a.usuario.id = :usuarioId " +
            "AND c.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY c.fecha ASC")
    List<Asistencia> findByUsuarioIdAndFechaBetween(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT COUNT(a) FROM Asistencia a " +
            "JOIN a.clase c " +
            "WHERE a.usuario.id = :usuarioId " +
            "AND a.presente = true " +
            "AND c.fecha BETWEEN :fechaInicio AND :fechaFin")
    Long countAsistenciasByUsuarioIdAndFechaBetween(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT a.usuario.id as usuarioId, CONCAT( a.usuario.nombre, ' ',  a.usuario.apellido) as nombre, COUNT(a) as cantidadAsistencias " +
            "FROM Asistencia a " +
            "JOIN a.clase c " +
            "WHERE a.presente = true " +
            "AND c.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY a.usuario.id, a.usuario.nombre, a.usuario.apellido " +
            "ORDER BY cantidadAsistencias DESC")
    List<Object[]> findUsuariosConMayorAsistenciaEnPeriodo(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT c.fecha as fecha, COUNT(a) as cantidadAsistencias " +
            "FROM Asistencia a " +
            "JOIN a.clase c " +
            "WHERE a.presente = true " +
            "AND c.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY c.fecha " +
            "ORDER BY cantidadAsistencias DESC")
    List<Object[]> countAsistenciaByFechaEnPeriodo(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}
