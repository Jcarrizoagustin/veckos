package com.veckos.VECKOS_Backend.runners;

import com.veckos.VECKOS_Backend.entities.*;
import com.veckos.VECKOS_Backend.repositories.PlanRepository;
import com.veckos.VECKOS_Backend.repositories.TurnoRepository;
import com.veckos.VECKOS_Backend.repositories.UsuarioRepository;
import com.veckos.VECKOS_Backend.security.repositories.RolRepository;
import com.veckos.VECKOS_Backend.security.repositories.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        inicializarRoles();

        // Crear usuario por defecto si no existe
        crearUsuarioAdmin();

        crearUsuario();

        crearPlan();

        crearTurnos();
    }

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

    public void crearUsuarioAdmin() {
        // Verificar si ya existe el usuario admin de forma más directa
        if (!usuarioSistemaRepository.existsByUsername("jmartinez")) {
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
                usuarioSistemaRepository.save(admin);

                System.out.println("Usuario administrador " + admin.getUsername() +  " creado correctamente.");
            } catch (Exception e) {
                System.err.println("Error al crear usuario administrador: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El usuario administrador ya existe en la base de datos.");
        }
    }

    public void crearUsuario(){
        List<Usuario> usuariosList = usuarioRepository.findAll();
        if(usuariosList.size() == 0){
            try{
                Usuario usuario = new Usuario();
                usuario.setTelefono("3834556633");
                usuario.setNombre("Agustin");
                usuario.setApellido("Carrizo");
                usuario.setCorreo("jcarrizo@test.com");
                usuario.setDni("12345678");
                usuario.setCuil("20123456780");
                usuario.setFechaNacimiento(LocalDate.of(1996, Month.OCTOBER, 26));
                usuario.setFechaAlta(LocalDateTime.now());
                this.usuarioRepository.save(usuario);
                System.err.println("Usuario  " + usuario.getNombre() + " creado correctamente");
            }catch(Exception ex){
                System.err.println("Error al crear usuario: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void crearPlan(){
        if(this.planRepository.findAll().size() == 0){
            try{
                Plan plan = new Plan();
                plan.setDescripcion("Descripcion plan 1");
                plan.setPrecio(BigDecimal.valueOf(29999));
                plan.setNombre("Wellness");
                this.planRepository.save(plan);
                System.out.println("Plan " + plan.getNombre() +  " creado correctamente.");
            }catch (Exception ex){
                System.err.println("Error al crear plan: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void crearTurnos(){
        if(this.turnoRepository.findAll().size() == 0){
            try{
                Turno turno1 = new Turno();
                turno1.setDiaSemana(DayOfWeek.MONDAY);
                turno1.setHora(LocalTime.of(22,00));

                Turno turno2 = new Turno();
                turno2.setDiaSemana(DayOfWeek.TUESDAY);
                turno2.setHora(LocalTime.of(22,00));

                Turno turno3 = new Turno();
                turno3.setDiaSemana(DayOfWeek.WEDNESDAY);
                turno3.setHora(LocalTime.of(22,00));

                Turno turno4 = new Turno();
                turno4.setDiaSemana(DayOfWeek.THURSDAY);
                turno4.setHora(LocalTime.of(22,00));

                Turno turno5 = new Turno();
                turno5.setDiaSemana(DayOfWeek.FRIDAY);
                turno5.setHora(LocalTime.of(22,00));

                this.turnoRepository.saveAll(Arrays.asList(turno1,turno2,turno3,turno4,turno5));

                System.out.println("Turnos creados correctamente.");
            }catch (Exception ex){
                System.err.println("Error al crear turnos: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
