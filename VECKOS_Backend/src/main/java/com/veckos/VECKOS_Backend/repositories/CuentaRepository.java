package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta,Long> {
}
