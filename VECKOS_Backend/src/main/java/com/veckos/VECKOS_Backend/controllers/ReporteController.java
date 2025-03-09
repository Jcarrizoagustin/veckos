package com.veckos.VECKOS_Backend.controllers;

import com.veckos.VECKOS_Backend.dtos.reporte.ReporteAsistenciaRequestDto;
import com.veckos.VECKOS_Backend.entities.Asistencia;
import com.veckos.VECKOS_Backend.entities.Clase;
import com.veckos.VECKOS_Backend.entities.Inscripcion;
import com.veckos.VECKOS_Backend.entities.Pago;
import com.veckos.VECKOS_Backend.services.AsistenciaService;
import com.veckos.VECKOS_Backend.services.ClaseService;
import com.veckos.VECKOS_Backend.services.InscripcionService;
import com.veckos.VECKOS_Backend.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReporteController {

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private ClaseService claseService;

    @Autowired
    private InscripcionService inscripcionService;

    @Autowired
    private PagoService pagoService;

    @PostMapping("/asistencia")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<?> generarReporteAsistencia(@RequestBody ReporteAsistenciaRequestDto requestDto) {
        Map<String, Object> reporte = new HashMap<>();

        // Datos básicos del reporte
        reporte.put("fechaInicio", requestDto.getFechaInicio());
        reporte.put("fechaFin", requestDto.getFechaFin());

        // Si es para un usuario específico
        if (requestDto.getUsuarioId() != null) {
            // Obtener asistencias del usuario en el período
            List<Asistencia> asistencias = asistenciaService.findByUsuarioIdAndFechaBetween(
                    requestDto.getUsuarioId(),
                    requestDto.getFechaInicio(),
                    requestDto.getFechaFin());

            // Filtrar por presentes si se solicita
            if (requestDto.getIncluirSoloPresentes()) {
                asistencias = asistencias.stream()
                        .filter(Asistencia::getPresente)
                        .collect(Collectors.toList());
            }

            // Calcular estadísticas
            Long totalAsistencias = asistenciaService.countAsistenciasByUsuarioIdAndFechaBetween(
                    requestDto.getUsuarioId(),
                    requestDto.getFechaInicio(),
                    requestDto.getFechaFin());

            // Obtener clases programadas para el usuario
            List<Clase> clasesUsuario = claseService.findClasesWithAsistenciaByUsuarioIdAndFechaBetween(
                    requestDto.getUsuarioId(),
                    requestDto.getFechaInicio(),
                    requestDto.getFechaFin());

            // Añadir datos al reporte
            reporte.put("usuarioId", requestDto.getUsuarioId());
            reporte.put("totalClasesProgramadas", clasesUsuario.size());
            reporte.put("totalAsistencias", totalAsistencias);

            // Calcular porcentaje de asistencia si hay clases programadas
            if (clasesUsuario.size() > 0) {
                double porcentaje = (totalAsistencias.doubleValue() / clasesUsuario.size()) * 100;
                reporte.put("porcentajeAsistencia", porcentaje);
            } else {
                reporte.put("porcentajeAsistencia", 0.0);
            }

            // Incluir lista de asistencias
            reporte.put("asistencias", asistencias);

        } else {
            // Reporte general
            // Obtener todos los usuarios con asistencias en el período
            List<Object[]> ranking = asistenciaService.findUsuariosConMayorAsistenciaEnPeriodo(
                    requestDto.getFechaInicio(),
                    requestDto.getFechaFin());

            // Obtener total de asistencias en el período
            Long totalAsistencias = claseService.countAsistenciasTotalesEnPeriodo(
                    requestDto.getFechaInicio(),
                    requestDto.getFechaFin());

            // Añadir datos al reporte
            reporte.put("rankingUsuarios", ranking);
            reporte.put("totalAsistencias", totalAsistencias);

            // Si se solicita agrupación por día
            if (requestDto.getAgruparPorDia()) {
                // Implementar agrupación por día
                // Esta lógica debería estar en un servicio específico
                reporte.put("asistenciasPorDia", "Datos agrupados por día");
            }
        }

        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/financiero")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> generarReporteFinanciero(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false, defaultValue = "false") Boolean agruparPorMes,
            @RequestParam(required = false, defaultValue = "false") Boolean agruparPorMetodoPago) {

        Map<String, Object> reporte = new HashMap<>();

        // Datos básicos del reporte
        reporte.put("fechaInicio", fechaInicio);
        reporte.put("fechaFin", fechaFin);

        // Obtener pagos en el período
        List<Pago> pagos = pagoService.findByFechaPagoBetween(fechaInicio, fechaFin);

        // Calcular suma total
        BigDecimal total = pagoService.sumMontoByFechaPagoBetween(fechaInicio, fechaFin);

        // Añadir datos al reporte
        reporte.put("cantidadPagos", pagos.size());
        reporte.put("ingresoTotal", total);

        // Calcular promedio si hay pagos
        if (pagos.size() > 0) {
            BigDecimal promedio = total.divide(BigDecimal.valueOf(pagos.size()), 2, BigDecimal.ROUND_HALF_UP);
            reporte.put("montoPromedio", promedio);
        } else {
            reporte.put("montoPromedio", BigDecimal.ZERO);
        }

        // Agrupar por mes si se solicita
        if (agruparPorMes) {
            List<Object[]> ingresosPorMes = pagoService.sumMontoByMesAndAnio(fechaInicio, fechaFin);
            reporte.put("ingresosPorMes", ingresosPorMes);
        }

        // Agrupar por método de pago si se solicita
        if (agruparPorMetodoPago) {
            List<Object[]> ingresosPorMetodo = pagoService.countPagosByMetodoPagoAndFechaPagoBetween(fechaInicio, fechaFin);
            reporte.put("ingresosPorMetodoPago", ingresosPorMetodo);
        }

        // Incluir lista de pagos
        reporte.put("pagos", pagos);

        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/inscripciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> generarReporteInscripciones(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        Map<String, Object> reporte = new HashMap<>();

        // Datos básicos del reporte
        reporte.put("fechaInicio", fechaInicio);
        reporte.put("fechaFin", fechaFin);

        // Obtener inscripciones en el período
        List<Inscripcion> inscripciones = inscripcionService.findByFechaFinBetween(fechaInicio, fechaFin);

        // Contar inscripciones
        reporte.put("totalInscripciones", inscripciones.size());

        // Agrupar por estado
        Map<Inscripcion.EstadoPago, Long> inscripcionesPorEstado = inscripciones.stream()
                .collect(Collectors.groupingBy(Inscripcion::getEstadoPago, Collectors.counting()));
        reporte.put("inscripcionesPorEstado", inscripcionesPorEstado);

        // Agrupar por frecuencia
        Map<Integer, Long> inscripcionesPorFrecuencia = inscripciones.stream()
                .collect(Collectors.groupingBy(Inscripcion::getFrecuencia, Collectors.counting()));
        reporte.put("inscripcionesPorFrecuencia", inscripcionesPorFrecuencia);

        // Incluir lista de inscripciones
        reporte.put("inscripciones", inscripciones);

        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<?> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());
        LocalDate inicioMesAnterior = hoy.minusMonths(1).withDayOfMonth(1);
        LocalDate finMesAnterior = inicioMes.minusDays(1);

        // Contar usuarios activos
        long usuariosActivos = inscripcionService.findByEstadoPago(Inscripcion.EstadoPago.ACTIVO).size();
        stats.put("usuariosActivos", usuariosActivos);

        // Contar usuarios próximos a vencer
        long usuariosProximosAVencer = inscripcionService.findByEstadoPago(Inscripcion.EstadoPago.PROXIMO_A_VENCER).size();
        stats.put("usuariosProximosAVencer", usuariosProximosAVencer);

        // Calcular ingresos
        BigDecimal ingresosMesActual = pagoService.sumMontoByFechaPagoBetween(inicioMes, finMes);
        BigDecimal ingresosMesAnterior = pagoService.sumMontoByFechaPagoBetween(inicioMesAnterior, finMesAnterior);
        stats.put("ingresosMesActual", ingresosMesActual);
        stats.put("ingresosMesAnterior", ingresosMesAnterior);

        // Asistencias de hoy
        List<Clase> clasesHoy = claseService.findByFechaOrderByHora(hoy);
        long asistenciasHoy = clasesHoy.stream()
                .flatMap(c -> c.getAsistencias().stream())
                .filter(Asistencia::getPresente)
                .count();
        stats.put("asistenciasHoy", asistenciasHoy);

        return ResponseEntity.ok(stats);
    }
}
