package com.veckos.VECKOS_Backend.controllers;

import com.veckos.VECKOS_Backend.dtos.pago.PagoCrearDto;
import com.veckos.VECKOS_Backend.entities.Pago;
import com.veckos.VECKOS_Backend.services.InscripcionService;
import com.veckos.VECKOS_Backend.services.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private InscripcionService inscripcionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Pago>> getAllPagos() {
        List<Pago> pagos = pagoService.findAll();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pago> getPagoById(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/inscripcion/{inscripcionId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Pago>> getPagosByInscripcionId(@PathVariable Long inscripcionId) {
        List<Pago> pagos = pagoService.findByInscripcionId(inscripcionId);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<Pago>> getPagosByUsuarioId(@PathVariable Long usuarioId) {
        List<Pago> pagos = pagoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Pago>> getPagosByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Pago> pagos = pagoService.findByFechaPagoBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(pagos);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pago> registrarPago(@Valid @RequestBody PagoCrearDto pagoDto) {
        // Validar que exista la inscripción
        inscripcionService.findById(pagoDto.getInscripcionId());

        // Convertir DTO a entidad
        Pago pago = new Pago();
        pago.setMonto(pagoDto.getMonto());
        pago.setFechaPago(pagoDto.getFechaPago() != null ? pagoDto.getFechaPago() : LocalDate.now());
        pago.setMetodoPago(pagoDto.getMetodoPago());
        pago.setDescripcion(pagoDto.getDescripcion());

        // Registrar el pago
        Pago registrado = pagoService.registrarPago(pagoDto.getInscripcionId(), pago);
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pago> updatePago(
            @PathVariable Long id,
            @Valid @RequestBody PagoCrearDto pagoDto) {

        // Convertir DTO a entidad
        Pago pago = new Pago();
        pago.setMonto(pagoDto.getMonto());
        pago.setFechaPago(pagoDto.getFechaPago());
        pago.setMetodoPago(pagoDto.getMetodoPago());
        pago.setDescripcion(pagoDto.getDescripcion());

        Pago updatedPago = pagoService.update(id, pago);
        return ResponseEntity.ok(updatedPago);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        pagoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estadisticas/periodo/suma")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> getSumaPagosPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        BigDecimal suma = pagoService.sumMontoByFechaPagoBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(suma);
    }

    @GetMapping("/estadisticas/periodo/metodo-pago")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getEstadisticasPorMetodoPago(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Object[]> estadisticas = pagoService.countPagosByMetodoPagoAndFechaPagoBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/estadisticas/periodo/mensual")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getEstadisticasMensuales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Object[]> estadisticas = pagoService.sumMontoByMesAndAnio(fechaInicio, fechaFin);
        return ResponseEntity.ok(estadisticas);
    }
}
