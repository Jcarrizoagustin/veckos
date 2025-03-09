package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.Turno;
import com.veckos.VECKOS_Backend.repositories.TurnoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    @Transactional(readOnly = true)
    public List<Turno> findAll() {
        return turnoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Turno findById(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turno no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<Turno> findByDiaSemanaAndHora(DayOfWeek diaSemana, LocalTime hora) {
        return turnoRepository.findByDiaSemanaAndHora(diaSemana, hora);
    }

    @Transactional
    public Turno save(Turno turno) {
        // Validar que no exista otro turno en el mismo día y hora
        if (turnoRepository.existsByDiaSemanaAndHora(turno.getDiaSemana(), turno.getHora())) {
            throw new IllegalStateException("Ya existe un turno para el día " +
                    turno.getDiaSemana() +
                    " a las " +
                    turno.getHora());
        }
        return turnoRepository.save(turno);
    }

    @Transactional
    public Turno update(Long id, Turno turnoDetails) {
        Turno turno = findById(id);

        // Validar que no exista otro turno en el mismo día y hora (excluyendo este mismo)
        if (!turno.getDiaSemana().equals(turnoDetails.getDiaSemana()) ||
                !turno.getHora().equals(turnoDetails.getHora())) {

            if (turnoRepository.existsByDiaSemanaAndHora(turnoDetails.getDiaSemana(), turnoDetails.getHora())) {
                throw new IllegalStateException("Ya existe un turno para el día " +
                        turnoDetails.getDiaSemana() +
                        " a las " +
                        turnoDetails.getHora());
            }
        }

        turno.setDiaSemana(turnoDetails.getDiaSemana());
        turno.setHora(turnoDetails.getHora());
        turno.setDescripcion(turnoDetails.getDescripcion());

        return turnoRepository.save(turno);
    }

    @Transactional
    public void deleteById(Long id) {
        Turno turno = findById(id);

        // Verificar si el turno tiene detalles de inscripción o clases antes de eliminar
        if (!turno.getDetallesInscripcion().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un turno con inscripciones asociadas");
        }

        if (!turno.getClases().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un turno con clases programadas");
        }

        turnoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Turno> findByDiaSemanaOrderByHoraAsc(DayOfWeek diaSemana) {
        return turnoRepository.findByDiaSemanaOrderByHoraAsc(diaSemana);
    }

    @Transactional(readOnly = true)
    public List<Turno> findTurnosByDiaSemanaConUsuarios(DayOfWeek diaSemana) {
        return turnoRepository.findTurnosByDiaSemanaConUsuarios(diaSemana);
    }

    @Transactional(readOnly = true)
    public List<Turno> findAllOrderByOcupacion() {
        return turnoRepository.findAllOrderByOcupacion();
    }
}
