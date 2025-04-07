package com.veckos.VECKOS_Backend.dtos.usuario;
import com.veckos.VECKOS_Backend.dtos.inscripcion.InscripcionInfoDto;
import com.veckos.VECKOS_Backend.entities.Inscripcion;
import com.veckos.VECKOS_Backend.entities.Usuario;
import com.veckos.VECKOS_Backend.enums.EstadoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDetalleDto {

    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String dni;
    private String cuil;
    private String telefono;
    private String correo;
    private LocalDateTime fechaAlta;
    private InscripcionInfoDto inscripcionActiva;
    private EstadoUsuario estadoUsuario;

    // Información adicional calculada
    private Integer edad;
    private Boolean tieneInscripcionActiva;

    // Constructor para convertir desde entidad y añadir información calculada
    public UsuarioDetalleDto(Usuario usuario, InscripcionInfoDto inscripcionActiva) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.fechaNacimiento = usuario.getFechaNacimiento();
        this.dni = usuario.getDni();
        this.cuil = usuario.getCuil();
        this.telefono = usuario.getTelefono();
        this.correo = usuario.getCorreo();
        this.fechaAlta = usuario.getFechaAlta();
        this.inscripcionActiva = inscripcionActiva;
        this.estadoUsuario = obtenerEstadoEstadoUsuario(usuario);

        // Calcular edad
        if (usuario.getFechaNacimiento() != null) {
            this.edad = Period.between(usuario.getFechaNacimiento(), LocalDate.now()).getYears();
        }

        this.tieneInscripcionActiva = inscripcionActiva != null && inscripcionActiva.getEstadoInscripcion().equals(Inscripcion.EstadoInscripcion.EN_CURSO);
    }

    private EstadoUsuario obtenerEstadoEstadoUsuario(Usuario usuario) {
        if(usuario.getInscripciones().size() == 0){
            return EstadoUsuario.PENDIENTE;
        }
        boolean esActivo = usuario.getInscripciones().stream()
                .anyMatch(inscripcion -> inscripcion.getEstadoInscripcion()
                        .equals(Inscripcion.EstadoInscripcion.EN_CURSO) && inscripcion.getEstadoPago().equals(Inscripcion.EstadoPago.PAGA));

        return esActivo ? EstadoUsuario.ACTIVO : EstadoUsuario.INACTIVO;
    }
}
