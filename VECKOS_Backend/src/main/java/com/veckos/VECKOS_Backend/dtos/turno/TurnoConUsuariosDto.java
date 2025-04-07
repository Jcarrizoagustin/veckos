package com.veckos.VECKOS_Backend.dtos.turno;

import com.veckos.VECKOS_Backend.dtos.usuario.UsuarioInfoDto;
import com.veckos.VECKOS_Backend.entities.Inscripcion;
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
public class TurnoConUsuariosDto {

    private Long id;
    private DayOfWeek diaSemana;
    private LocalTime hora;
    private String descripcion;
    private List<UsuarioInfoDto> usuarios;
    private Integer cantidadUsuarios;

    // Constructor para convertir desde entidad
    public TurnoConUsuariosDto(Turno turno) {
        this.id = turno.getId();
        this.diaSemana = turno.getDiaSemana();
        this.hora = turno.getHora();
        this.descripcion = turno.getDescripcion();
        this.usuarios = this.obtenerUsuarios(turno);
        this.cantidadUsuarios = usuarios.size();
    }

    private List<UsuarioInfoDto> obtenerUsuarios(Turno turno){
        List<UsuarioInfoDto> response = turno.getDetallesInscripcion()
                .stream()
                .filter(detalleInscripcion -> detalleInscripcion.getInscripcion().getEstadoInscripcion().equals(Inscripcion.EstadoInscripcion.EN_CURSO))
                .map(detalle -> new UsuarioInfoDto(detalle.getInscripcion().getUsuario())).toList();
        return response;
    }
}
