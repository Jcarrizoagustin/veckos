package com.veckos.VECKOS_Backend.dtos.reporte;

import com.veckos.VECKOS_Backend.dtos.pago.PagoInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO para un reporte financiero
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteFinancieroDto {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal ingresoTotal;
    private Integer cantidadPagos;
    private BigDecimal montoPromedio;
    //private Map<String, BigDecimal> ingresosPorMes;
    private List<Object[]> ingresosPorMetodoPago;
    private Map<String, BigDecimal> ingresosPorPlan;
    private List<PagoInfoDto> pagos;
}
