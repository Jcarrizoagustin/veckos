package com.veckos.VECKOS_Backend.security.service;

import com.veckos.VECKOS_Backend.entities.UsuarioSistema;
import com.veckos.VECKOS_Backend.security.repositories.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioSistemaRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioSistema usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("Usuario desactivado: " + username);
        }

        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre().toString()))
                .collect(Collectors.toList());

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                true,               // enabled
                true,               // accountNonExpired
                true,               // credentialsNonExpired
                true,               // accountNonLocked
                authorities
        );
    }
}
