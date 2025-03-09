package com.veckos.VECKOS_Backend.runners;

import com.veckos.VECKOS_Backend.entities.Rol;
import com.veckos.VECKOS_Backend.entities.UsuarioSistema;
import com.veckos.VECKOS_Backend.security.repositories.RolRepository;
import com.veckos.VECKOS_Backend.security.repositories.UsuarioSistemaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioSistemaRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        inicializarRoles();

        // Crear usuario por defecto si no existe
        crearUsuarioAdmin();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void inicializarRoles() {
        // Comprobar si hay roles de forma más directa
        List<Rol> roles = rolRepository.findAll();
        if (roles.isEmpty()) {
            try {
                // Crear rol de administrador
                Rol rolAdmin = new Rol();
                rolAdmin.setNombre(Rol.RolNombre.ROLE_ADMIN);
                rolRepository.save(rolAdmin);

                // Crear rol de operador
                Rol rolOperador = new Rol();
                rolOperador.setNombre(Rol.RolNombre.ROLE_OPERADOR);
                rolRepository.save(rolOperador);

                System.out.println("Roles inicializados correctamente.");
            } catch (Exception e) {
                System.err.println("Error al inicializar roles: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Los roles ya existen en la base de datos.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void crearUsuarioAdmin() {
        // Verificar si ya existe el usuario admin de forma más directa
        if (!usuarioRepository.existsByUsername("jmartinez")) {
            try {
                // Crear usuario administrador
                UsuarioSistema admin = new UsuarioSistema();
                admin.setNombre("Javier");
                admin.setApellido("Martinez");
                admin.setUsername("jmartinez");
                admin.setEmail("jmartinez@veckos-gym.com");
                admin.setPassword(passwordEncoder.encode("Veckos2025!"));
                admin.setActivo(true);

                // Asignar rol de administrador
                Set<Rol> roles = new HashSet<>();
                rolRepository.findByNombre(Rol.RolNombre.ROLE_ADMIN)
                        .ifPresent(roles::add);
                admin.setRoles(roles);

                // Guardar usuario
                usuarioRepository.save(admin);

                System.out.println("Usuario administrador (jmartinez) creado correctamente.");
            } catch (Exception e) {
                System.err.println("Error al crear usuario administrador: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El usuario administrador ya existe en la base de datos.");
        }
    }
}
