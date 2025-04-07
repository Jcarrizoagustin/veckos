package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.Inscripcion;
import com.veckos.VECKOS_Backend.entities.Pago;
import com.veckos.VECKOS_Backend.repositories.PagoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private InscripcionService inscripcionService;

    @Transactional(readOnly = true)
    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pago findById(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pago> findByInscripcionId(Long inscripcionId) {
        return pagoRepository.findByInscripcionId(inscripcionId);
    }

    @Transactional(readOnly = true)
    public List<Pago> findByUsuarioId(Long usuarioId) {
        return pagoRepository.findByInscripcionUsuarioId(usuarioId);
    }

    @Transactional
    public Pago registrarPago(Long inscripcionId, Pago pago) {
        Inscripcion inscripcion = inscripcionService.findById(inscripcionId);
        pago.setInscripcion(inscripcion);
        //inscripcionService.cambiarEstadoInscripcionAEnCurso(inscripcion, Inscripcion.EstadoInscripcion.EN_CURSO);
        if(inscripcionService.verificarInscripcionEstaEnCurso(inscripcion)){
            inscripcion.setEstadoInscripcion(Inscripcion.EstadoInscripcion.EN_CURSO);
        }

        // Si no se especifica la fecha de pago, usar la fecha actual
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDate.now());
        }

        // Guardar el pago
        Pago pagoPersistido = pagoRepository.save(pago);

        // Actualizar la inscripción
        inscripcion.setEstadoPago(Inscripcion.EstadoPago.PAGA);
        inscripcion.setUltimoPago(pago.getFechaPago());
        inscripcionService.update(inscripcionId, inscripcion);

        return pagoPersistido;
    }

    @Transactional
    public Pago update(Long id, Pago pagoDetails) {
        Pago pago = findById(id);

        // Actualizar solo los campos permitidos
        pago.setMonto(pagoDetails.getMonto());
        pago.setFechaPago(pagoDetails.getFechaPago());
        pago.setMetodoPago(pagoDetails.getMetodoPago());
        pago.setDescripcion(pagoDetails.getDescripcion());

        return pagoRepository.save(pago);
    }

    @Transactional
    public void deleteById(Long id) {
        Pago pago = findById(id);

        // Actualizar la inscripción si es necesario
        Inscripcion inscripcion = pago.getInscripcion();
        List<Pago> pagosDeLaInscripcion = pagoRepository.findByInscripcionId(inscripcion.getId());

        // Si este es el único pago, cambiar el estado
        if (pagosDeLaInscripcion.size() <= 1) {
            inscripcion.setEstadoPago(Inscripcion.EstadoPago.PENDIENTE);
            inscripcion.setUltimoPago(null);
            inscripcionService.update(inscripcion.getId(), inscripcion);
        }
        // Si hay más pagos, actualizar la fecha del último pago
        else {
            LocalDate ultimaFechaPago = pagosDeLaInscripcion.stream()
                    .filter(p -> !p.getId().equals(id))
                    .map(Pago::getFechaPago)
                    .max(LocalDate::compareTo)
                    .orElse(null);

            inscripcion.setUltimoPago(ultimaFechaPago);
            inscripcionService.update(inscripcion.getId(), inscripcion);
        }

        pagoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Pago> findByFechaPagoBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.findByFechaPagoBetweenOrderByFechaPagoDesc(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumMontoByFechaPagoBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        BigDecimal suma = pagoRepository.sumMontoByFechaPagoBetween(fechaInicio, fechaFin);
        return (suma != null) ? suma : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public List<Object[]> countPagosByMetodoPagoAndFechaPagoBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.sumPagosByMetodoPagoAndFechaPagoBetween(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<Object[]> sumMontoByMesAndAnio(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.sumMontoByMesAndAnio(fechaInicio, fechaFin);
    }
}
