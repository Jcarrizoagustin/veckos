package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.dtos.asistencia.AsistenciaInfoDto;
import com.veckos.VECKOS_Backend.dtos.pago.PagoInfoDto;
import com.veckos.VECKOS_Backend.dtos.reporte.ReporteAsistenciaGeneralDto;
import com.veckos.VECKOS_Backend.dtos.reporte.ReporteAsistenciaIndividualDto;
import com.veckos.VECKOS_Backend.dtos.reporte.ReporteAsistenciaRequestDto;
import com.veckos.VECKOS_Backend.dtos.reporte.ReporteFinancieroDto;
import com.veckos.VECKOS_Backend.entities.Asistencia;
import com.veckos.VECKOS_Backend.entities.Clase;
import com.veckos.VECKOS_Backend.entities.Pago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private ClaseService claseService;

    @Autowired
    private PagoService pagoService;

    public ReporteFinancieroDto generarReporteFinanciero(LocalDate fechaInicio, LocalDate fechaFin,
                                                        boolean agruparPorMes, boolean agruparPorMetodoPago){

        //Map<String, Object> reporte = new HashMap<>();
        ReporteFinancieroDto reporteFinancieroDto = new ReporteFinancieroDto();

        // Datos básicos del reporte
        //reporte.put("fechaInicio", fechaInicio);
        reporteFinancieroDto.setFechaInicio(fechaInicio);
        //reporte.put("fechaFin", fechaFin);
        reporteFinancieroDto.setFechaFin(fechaFin);

        // Obtener pagos en el período
        List<Pago> pagos = pagoService.findByFechaPagoBetween(fechaInicio, fechaFin);
        List<PagoInfoDto> pagoInfoDtos = pagos.stream()
                .map(PagoInfoDto::new).toList();

        // Calcular suma total
        BigDecimal total = pagoService.sumMontoByFechaPagoBetween(fechaInicio, fechaFin);

        // Añadir datos al reporte
        //reporte.put("cantidadPagos", pagos.size());
        reporteFinancieroDto.setCantidadPagos(pagos.size());
        //reporte.put("ingresoTotal", total);
        reporteFinancieroDto.setIngresoTotal(total);

        // Calcular promedio si hay pagos
        if (pagos.size() > 0) {
            BigDecimal promedio = total.divide(BigDecimal.valueOf(pagos.size()), 2, BigDecimal.ROUND_HALF_UP);
            reporteFinancieroDto.setMontoPromedio(promedio);
            //reporte.put("montoPromedio", promedio);
        } else {
            //reporte.put("montoPromedio", BigDecimal.ZERO);
            reporteFinancieroDto.setMontoPromedio(BigDecimal.ZERO);
        }

        // Agrupar por mes si se solicita
        if (agruparPorMes) {
            //List<Object[]> ingresosPorMes = pagoService.sumMontoByMesAndAnio(fechaInicio, fechaFin);
            //reporte.put("ingresosPorMes", ingresosPorMes);
            //reporteFinancieroDto.setIngresosPorMes(ingresosPorMes);
        }

        // Agrupar por método de pago si se solicita
        if (agruparPorMetodoPago) {
            List<Object[]> ingresosPorMetodo = pagoService.countPagosByMetodoPagoAndFechaPagoBetween(fechaInicio, fechaFin);
            //reporte.put("ingresosPorMetodoPago", ingresosPorMetodo);
            reporteFinancieroDto.setIngresosPorMetodoPago(ingresosPorMetodo);
        }

        // Incluir lista de pagos
        //reporte.put("pagos", pagos);
        reporteFinancieroDto.setPagos(pagoInfoDtos);


        return reporteFinancieroDto;
    }

    public ReporteAsistenciaIndividualDto generarReporteAsistenciaIndividual(ReporteAsistenciaRequestDto requestDto){
        ReporteAsistenciaIndividualDto reporteAsistenciaIndividualDto = new ReporteAsistenciaIndividualDto();

        reporteAsistenciaIndividualDto.setFechaInicio(requestDto.getFechaInicio().atStartOfDay());
        reporteAsistenciaIndividualDto.setFechaFin(requestDto.getFechaFin().atStartOfDay());
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
        Long totalAsistenciasUsuario = asistenciaService.countAsistenciasByUsuarioIdAndFechaBetween(
                requestDto.getUsuarioId(),
                requestDto.getFechaInicio(),
                requestDto.getFechaFin());

        // Obtener clases programadas para el usuario
        List<Clase> clasesUsuario = claseService.findClasesWithAsistenciaByUsuarioIdAndFechaBetween(
                requestDto.getUsuarioId(),
                requestDto.getFechaInicio(),
                requestDto.getFechaFin());

        // Añadir datos al reporte
        reporteAsistenciaIndividualDto.setUsuarioId(requestDto.getUsuarioId());
        reporteAsistenciaIndividualDto.setTotalClasesProgramadas(clasesUsuario.size());
        reporteAsistenciaIndividualDto.setTotalAsistencias(totalAsistenciasUsuario);

        // Calcular porcentaje de asistencia si hay clases programadas
        if (clasesUsuario.size() > 0) {
            double porcentaje = (totalAsistenciasUsuario.doubleValue() / clasesUsuario.size()) * 100;
            reporteAsistenciaIndividualDto.setPorcentajeAsistencia(porcentaje);
        } else {
            reporteAsistenciaIndividualDto.setPorcentajeAsistencia(0.0);
        }
        List<AsistenciaInfoDto> asistenciaInfo = asistencias.stream()
                        .map(AsistenciaInfoDto::new).toList();
        reporteAsistenciaIndividualDto.setAsistencias(asistenciaInfo);
        return reporteAsistenciaIndividualDto;
    }

    public ReporteAsistenciaGeneralDto generarReporteAsistenciaGeneral(ReporteAsistenciaRequestDto requestDto){
        ReporteAsistenciaGeneralDto reporteGeneralDto = new ReporteAsistenciaGeneralDto();
        reporteGeneralDto.setFechaInicio(requestDto.getFechaInicio().atStartOfDay());
        reporteGeneralDto.setFechaFin(requestDto.getFechaFin().atStartOfDay());
        List<Object[]> ranking = asistenciaService.findUsuariosConMayorAsistenciaEnPeriodo(
                requestDto.getFechaInicio(),
                requestDto.getFechaFin());

        // Obtener total de asistencias en el período
        Long totalAsistencias = claseService.countAsistenciasTotalesEnPeriodo(
                requestDto.getFechaInicio(),
                requestDto.getFechaFin());
        reporteGeneralDto.setRankingUsuarios(ranking);
        reporteGeneralDto.setTotalAsistencias(totalAsistencias.intValue());

        Long clasesProgramadas = claseService.findByFechaBetween(requestDto.getFechaInicio(), requestDto.getFechaFin()).stream().count();
        reporteGeneralDto.setTotalClasesProgramadas(clasesProgramadas.intValue());
        reporteGeneralDto.setPorcentajeAsistenciaPromedio(78.8);//TODO calcular porcentaje general

        if (requestDto.getAgruparPorDia()) {
            // Implementar agrupación por día
            Map<LocalDateTime,Long> asistenciasPorDia = new HashMap<>();
            List<Object[]> asistenciasPorDiaDataSet = asistenciaService.findCantidadAsistenciaByFechaEnPeriodo(requestDto.getFechaInicio()
                    ,requestDto.getFechaFin());
            for(Object[] item : asistenciasPorDiaDataSet){
                try{
                    LocalDate fecha = (LocalDate)item[0];
                    Long cantidad = (Long)item[1];
                    asistenciasPorDia.put(fecha.atStartOfDay(),cantidad);
                }catch (Exception ex){
                    System.out.println("ERROR: " + ex.getMessage());
                }
            }
            reporteGeneralDto.setAsistenciasPorDia(asistenciasPorDia);
        }
        return reporteGeneralDto;
    }
}
