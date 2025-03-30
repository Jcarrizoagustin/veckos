package com.veckos.VECKOS_Backend.dtos.usuario;
import com.veckos.VECKOS_Backend.dtos.inscripcion.InscripcionInfoDto;
import com.veckos.VECKOS_Backend.entities.Inscripcion;
import com.veckos.VECKOS_Backend.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Inscripcion.EstadoPago estado;
    private LocalDateTime fechaAlta;
    private InscripcionInfoDto inscripcionActiva;

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
        this.estado = usuario.obtenerEstado();
        this.fechaAlta = usuario.getFechaAlta();
        this.inscripcionActiva = inscripcionActiva;

        // Calcular edad
        if (usuario.getFechaNacimiento() != null) {
            this.edad = LocalDate.now().getYear() - usuario.getFechaNacimiento().getYear();
        }

        this.tieneInscripcionActiva = inscripcionActiva != null;
    }
}
