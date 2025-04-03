package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByInscripcionId(Long inscripcionId);

    List<Pago> findByInscripcionUsuarioId(Long usuarioId);

    List<Pago> findByFechaPagoBetweenOrderByFechaPagoDesc(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumMontoByFechaPagoBetween(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT p.metodoPago, SUM(p.monto) FROM Pago p " +
            "WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY p.metodoPago")
    List<Object[]> sumPagosByMetodoPagoAndFechaPagoBetween(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT FUNCTION('YEAR', p.fechaPago) as anio, " +
            "FUNCTION('MONTH', p.fechaPago) as mes, " +
            "SUM(p.monto) as total " +
            "FROM Pago p " +
            "WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY FUNCTION('YEAR', p.fechaPago), FUNCTION('MONTH', p.fechaPago) " +
            "ORDER BY anio, mes")
    List<Object[]> sumMontoByMesAndAnio(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}
