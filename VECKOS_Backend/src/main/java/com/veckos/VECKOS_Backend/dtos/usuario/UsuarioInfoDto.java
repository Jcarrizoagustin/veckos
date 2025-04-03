package com.veckos.VECKOS_Backend.dtos.usuario;

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
public class UsuarioInfoDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private LocalDateTime fechaNacimiento;
    private Inscripcion.EstadoPago estado;
    private LocalDateTime fechaAlta;

    // Constructor para convertir desde entidad
    public UsuarioInfoDto(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.dni = usuario.getDni();
        this.telefono = usuario.getTelefono();
        this.fechaNacimiento = usuario.getFechaNacimiento().atStartOfDay();
        this.estado = usuario.obtenerEstado();
        this.fechaAlta = usuario.getFechaAlta();
    }
}
