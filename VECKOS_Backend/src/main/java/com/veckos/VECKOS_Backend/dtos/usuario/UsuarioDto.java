package com.veckos.VECKOS_Backend.dtos.usuario;
import com.veckos.VECKOS_Backend.entities.Inscripcion;
import com.veckos.VECKOS_Backend.entities.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    private String cuil;

    private String telefono;

    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;

    private Inscripcion.EstadoPago estado;
}
