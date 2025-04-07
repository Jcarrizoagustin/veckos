package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.*;
import com.veckos.VECKOS_Backend.repositories.InscripcionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PlanService planService;

    @Autowired
    private ClaseService claseService;

    @Transactional(readOnly = true)
    public List<Inscripcion> findAll() {
        return inscripcionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Inscripcion findById(Long id) {
        return inscripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Inscripcion> findByUsuarioId(Long usuarioId) {
        return inscripcionRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public Optional<Inscripcion> findInscripcionActivaByUsuarioId(Long usuarioId) {
        return inscripcionRepository.findByUsuarioIdAndFechaFinGreaterThanEqual(usuarioId, LocalDate.now());
    }


    public Inscripcion save(Inscripcion inscripcion, Set<DetalleInscripcion> detalles) {
        // Verificar usuario y plan
        //Usuario usuario = usuarioService.findById(inscripcion.getUsuario().getId());
        //Plan plan = planService.findById(inscripcion.getPlan().getId());
        //Usuario usuario = inscripcion.getUsuario();
        //Plan plan = inscripcion.getPlan();

        // Lo de abajo no va xq repite logica, los objetos ya estan seteados
        //inscripcion.setUsuario(usuario);
        //inscripcion.setPlan(plan);

        // Establecer fecha de inicio y fin
        //Lo de abajo no va xq las fechas ya vienen seteadas del controller
        /*if (inscripcion.getFechaInicio() == null) {
            inscripcion.setFechaInicio(LocalDate.now());
        }

        if (inscripcion.getFechaFin() == null) {
            inscripcion.setFechaFin(inscripcion.getFechaInicio().plusMonths(1));
        }*/

        // Validar frecuencia (3 o 5 días)
        if (inscripcion.getFrecuencia() != 3 && inscripcion.getFrecuencia() != 5) {
            throw new IllegalArgumentException("La frecuencia debe ser 3 o 5 días por semana");
        }

        // Establecer estado de pago inicial
        //Lo de abajo no va xq el estado pago ya esta seteado como ACTIVO desde el controller
        /*if (inscripcion.getEstadoPago() == null) {
            inscripcion.setEstadoPago(Inscripcion.EstadoPago.ACTIVO);
        }*/

        // Guardar inscripción
        //Inscripcion savedInscripcion = inscripcionRepository.save(inscripcion);

        // Agregar detalles
        if (detalles != null) {
            for (DetalleInscripcion detalle : detalles) {
                //detalle.setInscripcion(inscripcion);
                inscripcion.addDetalle(detalle);
            }

            // Verificar que la cantidad de detalles coincida con la frecuencia
            if (detalles.size() != inscripcion.getFrecuencia()) {
                throw new IllegalArgumentException("El número de turnos seleccionados debe coincidir con la frecuencia");
            }
        }

        // Guardar con detalles
        Inscripcion inscripcionConDetalles = inscripcionRepository.save(inscripcion);

        // Generar clases para el período de inscripción
        List<Clase> clasesGeneradas = claseService.generarClasesParaInscripcion(inscripcionConDetalles);

        return inscripcionConDetalles;

       // return null;
    }

    @Transactional
    public Inscripcion update(Long id, Inscripcion inscripcionDetails) {
        Inscripcion inscripcion = findById(id);

        // Actualizar solo campos permitidos
        inscripcion.setFechaFin(inscripcionDetails.getFechaFin());
        inscripcion.setEstadoPago(inscripcionDetails.getEstadoPago());
        inscripcion.setUltimoPago(inscripcionDetails.getUltimoPago());

        // No permitimos cambiar el usuario, plan, fecha de inicio o frecuencia
        // para eso sería necesario cancelar y crear una nueva inscripción

        return inscripcionRepository.save(inscripcion);
    }

    @Transactional
    public Inscripcion renovarInscripcion(Long id) {
        Inscripcion inscripcionAnterior = findById(id);
        LocalDate hoy = LocalDate.now();

        if(!inscripcionAnterior.getEstadoPago().equals(Inscripcion.EstadoPago.PAGA)){
            throw new RuntimeException("Error: La inscripcion no se encuentra paga, no se puede renovar");
        }
        inscripcionAnterior.setEstadoInscripcion(Inscripcion.EstadoInscripcion.COMPLETADA);

        // Crear nueva inscripción basada en la anterior
        Inscripcion nuevaInscripcion = new Inscripcion();
        nuevaInscripcion.setUsuario(inscripcionAnterior.getUsuario());
        nuevaInscripcion.setPlan(inscripcionAnterior.getPlan());
        nuevaInscripcion.setFrecuencia(inscripcionAnterior.getFrecuencia());
        nuevaInscripcion.setFechaInicio(hoy);
        nuevaInscripcion.setFechaFin(hoy.plusMonths(1));
        nuevaInscripcion.setEstadoPago(Inscripcion.EstadoPago.PENDIENTE);
        nuevaInscripcion.setEstadoInscripcion(Inscripcion.EstadoInscripcion.EN_CURSO);

        //Inscripcion savedInscripcion = inscripcionRepository.save(nuevaInscripcion);

        // Copiar detalles de inscripción
        for (DetalleInscripcion detalleAnterior : inscripcionAnterior.getDetalles()) {
            DetalleInscripcion nuevoDetalle = new DetalleInscripcion();
            nuevoDetalle.setInscripcion(nuevaInscripcion);
            nuevoDetalle.setTurno(detalleAnterior.getTurno());
            nuevoDetalle.setDiaSemana(detalleAnterior.getDiaSemana());
            nuevaInscripcion.addDetalle(nuevoDetalle);
        }
        inscripcionRepository.save(inscripcionAnterior);
        Inscripcion inscripcionRenovada = inscripcionRepository.save(nuevaInscripcion);

        // Generar clases para el nuevo período
        claseService.generarClasesParaInscripcion(inscripcionRenovada);

        return inscripcionRenovada;
    }

    @Transactional
    public Inscripcion renovarInscripcionConCambios(Long id, Inscripcion nuevaInscripcion, Set<DetalleInscripcion> nuevosDetalles) {
        // Verificar que la inscripción anterior existe
        findById(id);

        // Usar el método save para crear una nueva inscripción con los cambios
        return save(nuevaInscripcion, nuevosDetalles);
    }

    @Transactional
    public void deleteById(Long id) {
        Inscripcion inscripcion = findById(id);

        // En lugar de eliminar físicamente, se podría cambiar el estado
        inscripcion.setEstadoPago(Inscripcion.EstadoPago.PENDIENTE);
        inscripcionRepository.save(inscripcion);
    }

    @Transactional(readOnly = true)
    public List<Inscripcion> findByEstadoPago(Inscripcion.EstadoPago estadoPago) {
        return inscripcionRepository.findByEstadoPago(estadoPago);
    }

    @Transactional(readOnly = true)
    public List<Inscripcion> findByFechaFinBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return inscripcionRepository.findByFechaFinBetween(fechaInicio.minusDays(1), fechaFin.plusDays(1));
    }

    @Transactional
    public void actualizarEstadosPagos() {
        LocalDate hoy = LocalDate.now();
        LocalDate proximoVencimiento = hoy.plusDays(3);

        // Obtener inscripciones activas
        List<Inscripcion> inscripcionesActivas = inscripcionRepository.findByEstadoPago(Inscripcion.EstadoPago.PAGA);

        for (Inscripcion inscripcion : inscripcionesActivas) {
            // Verificar si ya venció
            if (inscripcion.getFechaFin().isBefore(hoy)) {
                inscripcion.setEstadoInscripcion(Inscripcion.EstadoInscripcion.COMPLETADA);
            }
            inscripcionRepository.save(inscripcion);
        }
    }

    public void cambiarEstadoInscripcionAEnCurso(Inscripcion inscripcion, Inscripcion.EstadoInscripcion estadoInscripcion){
        if(verificarInscripcionEstaEnCurso(inscripcion)){
            inscripcion.setEstadoInscripcion(estadoInscripcion);
            inscripcionRepository.save(inscripcion);
        }else{
            throw new RuntimeException("Error: No se puede actualizar el estado inscripçion");
        }
    }

    public boolean verificarInscripcionEstaEnCurso(Inscripcion inscripcion){
        LocalDate hoy = LocalDate.now();
        boolean estaEnCurso = !hoy.isBefore(inscripcion.getFechaInicio()) && !hoy.isAfter(inscripcion.getFechaFin());
        return estaEnCurso;
    }

    public long cantidadInscripcionesProximasAVencer(){
        LocalDate hoy = LocalDate.now();
        List<Inscripcion> inscripcionList = this.inscripcionRepository.findAll()
                .stream().filter(inscripcion -> verificarInscripcionEstaEnCurso(inscripcion) && inscripcion.getEstadoPago().equals(Inscripcion.EstadoPago.PAGA) && ChronoUnit.DAYS.between(hoy, inscripcion.getFechaFin()) < 4).toList();
        return inscripcionList.size();
    }

    public void completarInscripcion(Long id) {
        Inscripcion inscripcion = this.inscripcionRepository.findById(id).get();
        if(inscripcion.getEstadoPago().equals(Inscripcion.EstadoPago.PAGA) && inscripcion.getEstadoInscripcion().equals(Inscripcion.EstadoInscripcion.EN_CURSO)){
            inscripcion.setEstadoInscripcion(Inscripcion.EstadoInscripcion.COMPLETADA);
            this.inscripcionRepository.save(inscripcion);
        }
        else{
            throw new RuntimeException("ERROR: La inscripcion no se encuentra en condiciones de ser completada");
        }
    }
}
