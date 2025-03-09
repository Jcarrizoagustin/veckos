package com.veckos.VECKOS_Backend.dtos.turno;

import com.veckos.VECKOS_Backend.dtos.usuario.UsuarioInfoDto;
import com.veckos.VECKOS_Backend.entities.Turno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TurnoConUsuariosDto {

    private Long id;
    private DayOfWeek diaSemana;
    private LocalTime hora;
    private String descripcion;
    private List<UsuarioInfoDto> usuarios;
    private Integer cantidadUsuarios;

    // Constructor para convertir desde entidad
    public TurnoConUsuariosDto(Turno turno, List<UsuarioInfoDto> usuarios) {
        this.id = turno.getId();
        this.diaSemana = turno.getDiaSemana();
        this.hora = turno.getHora();
        this.descripcion = turno.getDescripcion();
        this.usuarios = usuarios;
        this.cantidadUsuarios = usuarios.size();
    }
}
