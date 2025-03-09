package com.veckos.VECKOS_Backend.controllers;

import com.veckos.VECKOS_Backend.dtos.usuario.UsuarioDto;
import com.veckos.VECKOS_Backend.entities.Usuario;
import com.veckos.VECKOS_Backend.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        List<UsuarioDto> usuariosDto = usuarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDto);
    }

    @GetMapping("/activos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<UsuarioDto>> getUsuariosActivos() {
        List<Usuario> usuarios = usuarioService.findByEstado(Usuario.EstadoUsuario.ACTIVO);
        List<UsuarioDto> usuariosDto = usuarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<UsuarioDto> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok(convertToDto(usuario));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<UsuarioDto>> buscarUsuarios(@RequestParam String termino) {
        List<Usuario> usuarios = usuarioService.buscarPorTermino(termino);
        List<UsuarioDto> usuariosDto = usuarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> createUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        // Verificar si ya existe un usuario con el mismo DNI
        if (usuarioService.existsByDni(usuarioDto.getDni())) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = convertToEntity(usuarioDto);
        Usuario savedUsuario = usuarioService.save(usuario);
        return new ResponseEntity<>(convertToDto(savedUsuario), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> updateUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDto usuarioDto) {

        Usuario usuario = convertToEntity(usuarioDto);
        Usuario updatedUsuario = usuarioService.update(id, usuario);
        return ResponseEntity.ok(convertToDto(updatedUsuario));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convierte una entidad Usuario a su DTO correspondiente
     */
    private UsuarioDto convertToDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setDni(usuario.getDni());
        dto.setCuil(usuario.getCuil());
        dto.setTelefono(usuario.getTelefono());
        dto.setCorreo(usuario.getCorreo());
        dto.setEstado(usuario.getEstado());
        return dto;
    }

    /**
     * Convierte un DTO de Usuario a su entidad correspondiente
     */
    private Usuario convertToEntity(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDto.getId());
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setFechaNacimiento(usuarioDto.getFechaNacimiento());
        usuario.setDni(usuarioDto.getDni());
        usuario.setCuil(usuarioDto.getCuil());
        usuario.setTelefono(usuarioDto.getTelefono());
        usuario.setCorreo(usuarioDto.getCorreo());
        usuario.setEstado(usuarioDto.getEstado());
        return usuario;
    }
}