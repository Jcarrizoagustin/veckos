package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.Asistencia;
import com.veckos.VECKOS_Backend.entities.Clase;
import com.veckos.VECKOS_Backend.entities.Usuario;
import com.veckos.VECKOS_Backend.repositories.AsistenciaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private ClaseService claseService;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public List<Asistencia> findAll() {
        return asistenciaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Asistencia findById(Long id) {
        return asistenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Asistencia> findByUsuarioId(Long usuarioId) {
        return asistenciaRepository.findByUsuarioIdOrderByFechaRegistroDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Asistencia> findByClaseId(Long claseId) {
        return asistenciaRepository.findByClaseId(claseId);
    }

    @Transactional(readOnly = true)
    public Optional<Asistencia> findByClaseIdAndUsuarioId(Long claseId, Long usuarioId) {
        return asistenciaRepository.findByClaseIdAndUsuarioId(claseId, usuarioId);
    }

    @Transactional
    public Asistencia registrarAsistencia(Long claseId, Long usuarioId, Boolean presente) {
        // Verificar si ya existe una asistencia para este usuario y clase
        Optional<Asistencia> asistenciaExistente = findByClaseIdAndUsuarioId(claseId, usuarioId);

        if (asistenciaExistente.isPresent()) {
            // Actualizar la asistencia existente
            Asistencia asistencia = asistenciaExistente.get();
            asistencia.setPresente(presente);
            asistencia.setFechaRegistro(LocalDateTime.now());
            return asistenciaRepository.save(asistencia);
        } else {
            // Crear una nueva asistencia
            Clase clase = claseService.findById(claseId);
            Usuario usuario = usuarioService.findById(usuarioId);

            Asistencia nuevaAsistencia = new Asistencia();
            nuevaAsistencia.setClase(clase);
            nuevaAsistencia.setUsuario(usuario);
            nuevaAsistencia.setPresente(presente);
            nuevaAsistencia.setFechaRegistro(LocalDateTime.now());

            return asistenciaRepository.save(nuevaAsistencia);
        }
    }

    @Transactional
    public List<Asistencia> registrarAsistenciasPorClase(Long claseId, List<Long> usuariosPresentes) {
        // Obtener todos los usuarios con inscripciones activas que deberían asistir a esta clase
        Clase clase = claseService.findById(claseId);

        // Primero, marcar a todos como ausentes
        List<Asistencia> asistencias = asistenciaRepository.findByClaseId(claseId);
        for (Asistencia asistencia : asistencias) {
            asistencia.setPresente(false);
            asistencia.setFechaRegistro(LocalDateTime.now());
            asistenciaRepository.save(asistencia);
        }

        // Luego, marcar a los presentes
        for (Long usuarioId : usuariosPresentes) {
            registrarAsistencia(claseId, usuarioId, true);
        }

        return asistenciaRepository.findByClaseId(claseId);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!asistenciaRepository.existsById(id)) {
            throw new EntityNotFoundException("Asistencia no encontrada con ID: " + id);
        }
        asistenciaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Asistencia> findByUsuarioIdAndFechaBetween(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        return asistenciaRepository.findByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public Long countAsistenciasByUsuarioIdAndFechaBetween(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        return asistenciaRepository.countAsistenciasByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<Object[]> findUsuariosConMayorAsistenciaEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        return asistenciaRepository.findUsuariosConMayorAsistenciaEnPeriodo(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<Object[]> findCantidadAsistenciaByFechaEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        return asistenciaRepository.countAsistenciaByFechaEnPeriodo(fechaInicio, fechaFin);
    }
}
