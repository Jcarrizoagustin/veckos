package com.veckos.VECKOS_Backend.dtos.cuenta;

import com.veckos.VECKOS_Backend.entities.Cuenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDto {

    private Long id;
    private String cbu;
    private String descripcion;

    public CuentaDto(Cuenta cuenta){
        this.id = cuenta.getId();
        this.cbu = cuenta.getCbu();
        this.descripcion = cuenta.getDescripcion();
    }
}
