package com.veckos.VECKOS_Backend.runners;

import com.veckos.VECKOS_Backend.entities.*;
import com.veckos.VECKOS_Backend.repositories.CuentaRepository;
import com.veckos.VECKOS_Backend.repositories.PlanRepository;
import com.veckos.VECKOS_Backend.repositories.TurnoRepository;
import com.veckos.VECKOS_Backend.repositories.UsuarioRepository;
import com.veckos.VECKOS_Backend.security.repositories.RolRepository;
import com.veckos.VECKOS_Backend.security.repositories.UsuarioSistemaRepository;
import org.jfree.data.time.Week;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

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

    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        inicializarRoles();

        // Crear usuario por defecto si no existe
        crearUsuarioAdmin();

        crearUsuario();

        crearPlan();

        crearTurnos();

        crearCuenta();
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
                Usuario usuario = crearUsuario("Agustin","Carrizo","jcarrizo@test.com","3834818181","12345678","20123456780",LocalDate.of(1996,Month.OCTOBER,26));
                Usuario usuario2 = crearUsuario("Jose","Sosa","jsosa@test.com","3834818182","12345679","20123456790",LocalDate.of(2000,Month.MAY,15));
                Usuario usuario3 = crearUsuario("Mariana","Lopez","mlopeza@test.com","3834838383","12345671","20123456710",LocalDate.of(1990,Month.DECEMBER,27));
                Usuario usuario4 = crearUsuario("Gimena","Carrizo","gcarrizo@test.com","3834242424","12345672","20123456720",LocalDate.of(1998,Month.APRIL, 2));
                this.usuarioRepository.save(usuario);
                this.usuarioRepository.save(usuario2);
                this.usuarioRepository.save(usuario3);
                this.usuarioRepository.save(usuario4);
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
                plan.setPrecio(BigDecimal.valueOf(29000));
                plan.setNombre("Wellness");
                Plan plan2 = new Plan();
                plan2.setDescripcion("Descripcion plan 2");
                plan2.setPrecio(BigDecimal.valueOf(34000));
                plan2.setNombre("Fitness");
                this.planRepository.save(plan);
                this.planRepository.save(plan2);
                System.out.println("Plan " + plan.getNombre() +  " creado correctamente.");
                System.out.println("Plan " + plan2.getNombre() +  " creado correctamente.");
            }catch (Exception ex){
                System.err.println("Error al crear plan: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void crearCuenta(){
        if(cuentaRepository.findAll().size() == 0){
            Cuenta cuenta = new Cuenta();
            cuenta.setCbu("056282822885884");
            cuenta.setDescripcion("Naranja X");
            this.cuentaRepository.save(cuenta);
            System.out.println("Cuenta: " + cuenta.getDescripcion() + " creada correctamente");
            Cuenta cuenta2 = new Cuenta();
            cuenta2.setCbu("05628287588282");
            cuenta2.setDescripcion("Santander");
            this.cuentaRepository.save(cuenta2);
            System.out.println("Cuenta: " + cuenta2.getDescripcion() + " creada correctamente");

        }
    }

    public void crearTurnos(){
        if(this.turnoRepository.findAll().size() == 0){
            try{
                List<Turno> turnosList1 = generarTurnos(LocalTime.of(07,30));
                List<Turno> turnosList2 = generarTurnos(LocalTime.of(10,00));
                List<Turno> turnosList3 = generarTurnos(LocalTime.of(20,00));
                List<Turno> turnosList4 = generarTurnos(LocalTime.of(21,00));

                this.turnoRepository.saveAll(turnosList1);
                this.turnoRepository.saveAll(turnosList2);
                this.turnoRepository.saveAll(turnosList3);
                this.turnoRepository.saveAll(turnosList4);

                System.out.println("Turnos creados correctamente.");
            }catch (Exception ex){
                System.err.println("Error al crear turnos: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private Usuario crearUsuario(String nombre, String apellido, String correo, String telefono, String dni, String cuil, LocalDate fechaNacimiento){
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setTelefono(telefono);
        usuario.setDni(dni);
        usuario.setCuil(cuil);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setFechaAlta(LocalDateTime.now());
        return usuario;
    }

    private List<Turno> generarTurnos(LocalTime hora){
        DayOfWeek[] diasSemana = {DayOfWeek.MONDAY,DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY,DayOfWeek.THURSDAY,DayOfWeek.FRIDAY};
        List<Turno> turnosList = new ArrayList<>();
        for(DayOfWeek day : diasSemana){
            Turno turno = new Turno();
            turno.setHora(hora);
            turno.setDiaSemana(day);
            turno.setDescripcion("Turno generado en inicializador");
            turnosList.add(turno);
        }
        return turnosList;
    }
}
