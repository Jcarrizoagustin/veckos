package com.veckos.VECKOS_Backend.dtos.usuario;

import com.veckos.VECKOS_Backend.entities.Inscripcion;
import com.veckos.VECKOS_Backend.entities.Usuario;
import com.veckos.VECKOS_Backend.enums.EstadoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioListItemDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private Integer edad;
    private String planActivo;
    private EstadoUsuario estado;

    // Constructor para convertir desde entidad
    public UsuarioListItemDto(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.dni = usuario.getDni();

        // Calcular edad
        if (usuario.getFechaNacimiento() != null) {
            this.edad = LocalDate.now().getYear() - usuario.getFechaNacimiento().getYear();
        }

        this.planActivo = planActivo;
        this.estado = obtenerEstadoEstadoUsuario(usuario);
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
