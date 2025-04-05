package com.veckos.VECKOS_Backend.dtos.pago;

import com.veckos.VECKOS_Backend.entities.Pago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para mostrar información de un pago
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoInfoDto {

    private Long id;
    private Long inscripcionId;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String dniUsuario;
    private String cuilUsuario;
    private String nombrePlan;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private Pago.MetodoPago metodoPago;
    private String cbuCuenta;
    private String descripcionCuenta;
    private String descripcion;

    // Constructor para convertir desde entidad
    public PagoInfoDto(Pago pago) {
        this.id = pago.getId();
        this.inscripcionId = pago.getInscripcion().getId();
        this.nombreUsuario = pago.getInscripcion().getUsuario().getNombre();
        this.apellidoUsuario = pago.getInscripcion().getUsuario().getApellido();
        this.nombrePlan = pago.getInscripcion().getPlan().getNombre();
        this.monto = pago.getMonto();
        this.fechaPago = pago.getFechaPago().atStartOfDay();
        this.metodoPago = pago.getMetodoPago();
        this.descripcion = pago.getDescripcion();
        this.dniUsuario = pago.getInscripcion().getUsuario().getDni();
        this.cuilUsuario = pago.getInscripcion().getUsuario().getCuil();
        if(pago.getMetodoPago().equals(Pago.MetodoPago.TRANSFERENCIA)){
            this.cbuCuenta = pago.getCuenta().getCbu();
            this.descripcionCuenta = pago.getCuenta().getDescripcion();
        }else{
            this.cbuCuenta = "";
            this.descripcionCuenta = "";
        }
    }
}
