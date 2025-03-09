package com.veckos.VECKOS_Backend.security.repositories;

import com.veckos.VECKOS_Backend.entities.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Long> {
    Optional<UsuarioSistema> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
