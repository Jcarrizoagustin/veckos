package com.veckos.VECKOS_Backend.dtos.usuario;

import com.veckos.VECKOS_Backend.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
class UsuarioListItemDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private Integer edad;
    private String planActivo;
    private Usuario.EstadoUsuario estado;

    // Constructor para convertir desde entidad
    public UsuarioListItemDto(Usuario usuario, String planActivo) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.dni = usuario.getDni();

        // Calcular edad
        if (usuario.getFechaNacimiento() != null) {
            this.edad = LocalDate.now().getYear() - usuario.getFechaNacimiento().getYear();
        }

        this.planActivo = planActivo;
        this.estado = usuario.getEstado();
    }
}
