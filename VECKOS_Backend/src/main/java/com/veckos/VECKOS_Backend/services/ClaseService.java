package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.Clase;
import com.veckos.VECKOS_Backend.entities.DetalleInscripcion;
import com.veckos.VECKOS_Backend.entities.Inscripcion;
import com.veckos.VECKOS_Backend.entities.Turno;
import com.veckos.VECKOS_Backend.repositories.ClaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private TurnoService turnoService;

    @Transactional(readOnly = true)
    public List<Clase> findAll() {
        return claseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Clase findById(Long id) {
        return claseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Clase> findByTurnoId(Long turnoId) {
        return claseRepository.findByTurnoId(turnoId);
    }

    //@Transactional(readOnly = true)
    public Optional<Clase> findByTurnoIdAndFecha(Long turnoId, LocalDate fecha) {
        return claseRepository.findByTurnoIdAndFecha(turnoId, fecha);
    }

    @Transactional
    public Clase save(Clase clase) {
        // Validar que no exista otra clase para el mismo turno y fecha
        if (claseRepository.findByTurnoIdAndFecha(clase.getTurno().getId(), clase.getFecha()).isPresent()) {
            throw new IllegalStateException("Ya existe una clase para el turno y fecha especificados");
        }

        // Asegurarse de que el turno existe
        Turno turno = turnoService.findById(clase.getTurno().getId());
        clase.setTurno(turno);

        return claseRepository.save(clase);
    }

    @Transactional
    public Clase update(Long id, Clase claseDetails) {
        Clase clase = findById(id);

        // Solo permitimos actualizar la descripción
        clase.setDescripcion(claseDetails.getDescripcion());

        return claseRepository.save(clase);
    }

    @Transactional
    public void deleteById(Long id) {
        Clase clase = findById(id);

        // Verificar si la clase tiene asistencias antes de eliminar
        if (!clase.getAsistencias().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar una clase con asistencias registradas");
        }

        claseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Clase> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return claseRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<Clase> findByFechaOrderByHora(LocalDate fecha) {
        return claseRepository.findByFechaOrderByHora(fecha);
    }

    //@Transactional
    public List<Clase> generarClasesParaInscripcion(Inscripcion inscripcion) {
        List<Clase> clasesGeneradas = new ArrayList<>();

        // Obtener detalles de la inscripción (días y turnos)
        List<DetalleInscripcion> detalles = inscripcion.getDetalles();

        // Período de la inscripción
        LocalDate fechaInicio = inscripcion.getFechaInicio();
        LocalDate fechaFin = inscripcion.getFechaFin();

        // Generar todas las fechas dentro del período
        LocalDate fechaActual = fechaInicio;
        while (!fechaActual.isAfter(fechaFin)) {
            DayOfWeek diaSemanaActual = fechaActual.getDayOfWeek();

            // Buscar si hay un detalle para este día de la semana
            for (DetalleInscripcion detalle : detalles) {
                if (detalle.getDiaSemana() == diaSemanaActual) {
                    // Verificar si ya existe la clase para este turno y fecha
                    Optional<Clase> claseExistente = findByTurnoIdAndFecha(detalle.getTurno().getId(), fechaActual);

                    if (claseExistente.isEmpty()) {
                        // Si no existe, crear la clase
                        Clase nuevaClase = new Clase();
                        nuevaClase.setTurno(detalle.getTurno());
                        nuevaClase.setFecha(fechaActual);
                        nuevaClase.setDescripcion("Clase generada automáticamente");

                        Clase claseGuardada = claseRepository.save(nuevaClase);
                        clasesGeneradas.add(claseGuardada);
                    } else {
                        // Si ya existe, agregarla a la lista de generadas
                        clasesGeneradas.add(claseExistente.get());
                    }
                }
            }

            // Avanzar al siguiente día
            fechaActual = fechaActual.plusDays(1);
        }

        return clasesGeneradas;
    }

    @Transactional(readOnly = true)
    public List<Clase> findClasesWithAsistenciaByUsuarioIdAndFechaBetween(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Clase> clases = claseRepository.findClasesWithAsistenciaByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin);
        return clases;
    }

    @Transactional(readOnly = true)
    public Long countAsistenciasTotalesEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        return claseRepository.countAsistenciasTotalesEnPeriodo(fechaInicio, fechaFin);
    }

    /*public List<Clase> buscarClasesParaFecha(LocalDate fecha) {
        List<Turno> turnos = claseRepository.findByFechaOrderByHora(fecha)
                .stream().map(clase -> clase.getTurno()).toList();

        return new ArrayList<>();
    }*/
}
