package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.Inscripcion;
import com.veckos.VECKOS_Backend.entities.Usuario;
import com.veckos.VECKOS_Backend.enums.EstadoUsuario;
import com.veckos.VECKOS_Backend.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    //@Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setFechaAlta(LocalDateTime.now());
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Long id, Usuario usuarioDetails) {
        Usuario usuario = findById(id);

        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setApellido(usuarioDetails.getApellido());
        usuario.setFechaNacimiento(usuarioDetails.getFechaNacimiento());
        usuario.setTelefono(usuarioDetails.getTelefono());
        usuario.setCorreo(usuarioDetails.getCorreo());
        //usuario.setEstado(usuarioDetails.getEstado());

        // No actualizamos DNI ni CUIL para evitar problemas de integridad

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteById(Long id) {
        Usuario usuario = findById(id);
        //usuario.setEstado(Usuario.EstadoUsuario.INACTIVO);
        usuarioRepository.save(usuario);
        // No eliminamos físicamente para mantener la integridad de los datos
    }

    /*@Transactional(readOnly = true)
    public List<Usuario> findByEstado(Usuario.EstadoUsuario estado) {
        return usuarioRepository.findByEstado(estado);
    }*/

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosActivos(){
        return usuarioRepository.findUsuariosActivos();
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosPendientes(){
        return usuarioRepository.findAll().stream()
                .filter(usuario -> Objects.isNull(usuario.obtenerInscripcionActiva()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarPorTermino(String termino) {
        return usuarioRepository.buscarPorTermino(termino);
    }

    @Transactional(readOnly = true)
    public List<Usuario> findConInscripcionActiva() {
        return usuarioRepository.findConInscripcionActivaEnFecha(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<Usuario> findConPagoProximoAVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate enUnaSemana = hoy.plusDays(7);
        return usuarioRepository.findConPagoProximoAVencer(hoy, enUnaSemana);
    }

    @Transactional(readOnly = true)
    public boolean existsByDni(String dni) {
        return usuarioRepository.existsByDni(dni);
    }

    public long cantidadUsuariosActivos(){
        List<Usuario> usuarios = this.usuarioRepository.findAll();
        return usuarios.stream()
                .filter(usuario -> determinarEstadoUsuario(usuario).equals(EstadoUsuario.ACTIVO )).count();
    }

    public EstadoUsuario determinarEstadoUsuario(Usuario usuario){
        if(usuario.getInscripciones().size() == 0){
            return EstadoUsuario.PENDIENTE;
        }
        boolean esActivo = usuario.getInscripciones().stream()
                .anyMatch(inscripcion -> inscripcion.getEstadoInscripcion()
                        .equals(Inscripcion.EstadoInscripcion.EN_CURSO) && inscripcion.getEstadoPago().equals(Inscripcion.EstadoPago.PAGA));

        return esActivo ? EstadoUsuario.ACTIVO : EstadoUsuario.INACTIVO;
    }
}
