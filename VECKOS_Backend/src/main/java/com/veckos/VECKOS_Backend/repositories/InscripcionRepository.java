package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByUsuarioId(Long usuarioId);

    Optional<Inscripcion> findByUsuarioIdAndFechaFinGreaterThanEqual(Long usuarioId, LocalDate fecha);

    List<Inscripcion> findByEstadoPago(Inscripcion.EstadoPago estadoPago);

    @Query("SELECT i FROM Inscripcion i WHERE i.fechaFin BETWEEN :fechaInicio AND :fechaFin")
    List<Inscripcion> findByFechaFinBetween(@Param("fechaInicio") LocalDate fechaInicio,
                                            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT i FROM Inscripcion i WHERE i.usuario.id = :usuarioId ORDER BY i.fechaInicio DESC")
    List<Inscripcion> findByUsuarioIdOrderByFechaInicioDesc(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.fechaInicio BETWEEN :fechaInicio AND :fechaFin")
    Long countInscripcionesEnPeriodo(@Param("fechaInicio") LocalDate fechaInicio,
                                     @Param("fechaFin") LocalDate fechaFin);
}