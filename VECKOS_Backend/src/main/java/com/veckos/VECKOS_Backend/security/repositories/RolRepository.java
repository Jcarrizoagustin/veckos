package com.veckos.VECKOS_Backend.security.repositories;

import com.veckos.VECKOS_Backend.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(Rol.RolNombre nombre);
}
