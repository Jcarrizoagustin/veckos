package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByDni(String dni);

    boolean existsByDni(String dni);

    boolean existsByCuil(String cuil);

    //List<Usuario> findByEstado(Usuario.EstadoUsuario estado);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) " +
            "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :termino, '%')) " +
            "OR u.dni LIKE CONCAT('%', :termino, '%')")
    List<Usuario> buscarPorTermino(@Param("termino") String termino);

    @Query("SELECT u FROM Usuario u JOIN u.inscripciones i WHERE i.fechaFin > :fecha")
    List<Usuario> findConInscripcionActivaEnFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT u FROM Usuario u JOIN u.inscripciones i WHERE i.estadoPago = 'PROXIMO_A_VENCER' " +
            "AND i.fechaFin BETWEEN :fechaInicio AND :fechaFin")
    List<Usuario> findConPagoProximoAVencer(@Param("fechaInicio") LocalDate fechaInicio,
                                            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT u FROM Usuario u JOIN u.inscripciones i WHERE i.estadoPago = 'ACTIVO' ")
    List<Usuario> findUsuariosActivos();

    //@Query("SELECT u FROM Usuario u JOIN u.inscripciones i WHERE i.usuario is NULL ")
    //List<Usuario> findUsuariosWhere();
}
