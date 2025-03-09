// src/app/features/turnos/turno-usuarios/turno-usuarios.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-turno-usuarios',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatSnackBarModule,
    MatDialogModule,
    MatCardModule,
    MatDividerModule,
    MatTooltipModule
  ],
  templateUrl: './turno-usuarios.component.html',
  styleUrls: ['./turno-usuarios.component.css']
})
export class TurnoUsuariosComponent implements OnInit {
  turnoId: number = 0;
  turnoInfo: any = null;
  isLoading: boolean = true;
  
  usuarios: any[] = [];
  filteredUsuarios: any[] = [];
  searchTerm: string = '';
  
  displayedColumns: string[] = ['avatar', 'nombre', 'plan', 'estado', 'fechaInicio', 'asistencia', 'acciones'];
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) { }
  
  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.turnoId = +params['id']; // Convertir a número
      this.loadTurnoInfo();
      this.loadUsuarios();
    });
  }
  
  loadTurnoInfo(): void {
    // Simulación de carga de datos desde el backend
    /*
    API endpoint: /api/turnos/{id}
    Estructura esperada:
    {
      "id": 1,
      "diaSemana": "MONDAY",
      "hora": "07:30:00",
      "descripcion": "Turno mañana",
      "usuariosInscritos": 15,
      "capacidadMaxima": 20
    }
    */
    
    setTimeout(() => {
      this.turnoInfo = {
        id: this.turnoId,
        diaSemana: 'MONDAY',
        nombreDiaSemana: 'Lunes',
        hora: '07:30:00',
        horaFormateada: '7:30 AM',
        descripcion: 'Turno mañana',
        usuariosInscritos: 15,
        capacidadMaxima: 20
      };
      
      this.isLoading = false;
    }, 1000);
  }
  
  loadUsuarios(): void {
    // Simulación de carga de datos desde el backend
    /*
    API endpoint: /api/turnos/{id}/usuarios
    Estructura esperada:
    [
      {
        "id": 1,
        "nombre": "Luciana",
        "apellido": "Lopez",
        "fechaNacimiento": "1991-05-15",
        "dni": "33123456",
        "edad": 33,
        "plan": "Wellness",
        "estado": "ACTIVO",
        "fechaInicio": "10/11/2023",
        "asistenciaUltimaSemana": [true, true, false, true, true] // últimas 5 clases
      },
      ...
    ]
    */
    
    setTimeout(() => {
      this.usuarios = [
        {
          id: 1,
          nombre: 'Luciana',
          apellido: 'Lopez',
          fechaNacimiento: '1991-05-15',
          dni: '33123456',
          edad: 33,
          plan: 'Wellness',
          estado: 'ACTIVO',
          fechaInicio: '10/11/2023',
          asistenciaUltimaSemana: [true, true, false, true, true]
        },
        {
          id: 2,
          nombre: 'Lautaro',
          apellido: 'Ruíñez',
          fechaNacimiento: '1990-07-22',
          dni: '33654987',
          edad: 34,
          plan: 'Fitness',
          estado: 'ACTIVO',
          fechaInicio: '15/12/2023',
          asistenciaUltimaSemana: [true, false, true, true, true]
        },
        {
          id: 3,
          nombre: 'Flor',
          apellido: 'Acosta',
          fechaNacimiento: '1995-03-10',
          dni: '37765432',
          edad: 29,
          plan: 'Wellness',
          estado: 'ACTIVO',
          fechaInicio: '05/01/2024',
          asistenciaUltimaSemana: [true, true, true, true, true]
        },
        {
          id: 4,
          nombre: 'Andrea',
          apellido: 'Quiroga',
          fechaNacimiento: '1988-11-30',
          dni: '32145678',
          edad: 36,
          plan: 'Sport',
          estado: 'INACTIVO',
          fechaInicio: '22/09/2023',
          asistenciaUltimaSemana: [false, false, false, false, false]
        },
        {
          id: 5,
          nombre: 'Carla',
          apellido: 'Sabina',
          fechaNacimiento: '1993-08-17',
          dni: '35789654',
          edad: 31,
          plan: 'Wellness',
          estado: 'ACTIVO',
          fechaInicio: '03/02/2024',
          asistenciaUltimaSemana: [true, false, true, false, true]
        }
      ];
      
      this.filteredUsuarios = [...this.usuarios];
    }, 1500);
  }
  
  filterUsuarios(): void {
    if (!this.searchTerm) {
      this.filteredUsuarios = [...this.usuarios];
      return;
    }
    
    const search = this.searchTerm.toLowerCase().trim();
    this.filteredUsuarios = this.usuarios.filter(usuario => 
      usuario.dni.includes(search) || 
      usuario.nombre.toLowerCase().includes(search) || 
      usuario.apellido.toLowerCase().includes(search)
    );
  }
  
  getEstadoClass(estado: string): string {
    switch (estado) {
      case 'ACTIVO': return 'estado-activo';
      case 'INACTIVO': return 'estado-inactivo';
      case 'PENDIENTE': return 'estado-pendiente';
      default: return '';
    }
  }
  
  getAsistenciaClass(presente: boolean): string {
    return presente ? 'asistencia-presente' : 'asistencia-ausente';
  }
  
  viewUsuario(id: number): void {
    this.router.navigate(['/dashboard/usuarios', id]);
  }
  
  removeUserFromTurno(id: number): void {
    // Simulación de llamada al backend para eliminar usuario del turno
    /*
    API endpoint: DELETE /api/turnos/{turnoId}/usuarios/{usuarioId}
    */
    
    if (confirm('¿Está seguro que desea eliminar este usuario del turno?')) {
      this.usuarios = this.usuarios.filter(u => u.id !== id);
      this.filteredUsuarios = this.filteredUsuarios.filter(u => u.id !== id);
      
      this.turnoInfo.usuariosInscritos--;
      
      this.snackBar.open('Usuario eliminado del turno exitosamente', 'Cerrar', {
        duration: 3000
      });
    }
  }
  
  addUserToTurno(): void {
    // Aquí implementaríamos la lógica para abrir un diálogo y agregar usuarios al turno
    // Por simplicidad, simulamos la adición de un usuario
    
    const newUser = {
      id: 6,
      nombre: 'Samuel',
      apellido: 'Cordoba',
      fechaNacimiento: '1991-01-15',
      dni: '33987654',
      edad: 33,
      plan: 'Fitness',
      estado: 'ACTIVO',
      fechaInicio: '01/03/2024',
      asistenciaUltimaSemana: [true, true, true, false, true]
    };
    
    this.usuarios.push(newUser);
    this.filteredUsuarios = [...this.usuarios];
    this.turnoInfo.usuariosInscritos++;
    
    this.snackBar.open('Usuario agregado al turno exitosamente', 'Cerrar', {
      duration: 3000
    });
  }
  
  registrarAsistencia(usuarioId: number, asistio: boolean): void {
    // Simulación de llamada al backend para registrar asistencia
    /*
    API endpoint: POST /api/asistencias
    Body: {
      claseId: X,
      usuarioId: usuarioId,
      presente: asistio
    }
    */
    
    const usuario = this.usuarios.find(u => u.id === usuarioId);
    if (usuario) {
      // Actualizar la asistencia más reciente (primera posición)
      usuario.asistenciaUltimaSemana.pop();
      usuario.asistenciaUltimaSemana.unshift(asistio);
      
      this.snackBar.open(
        `Asistencia ${asistio ? 'registrada' : 'removida'} para ${usuario.nombre} ${usuario.apellido}`, 
        'Cerrar', 
        { duration: 3000 }
      );
    }
  }
  
  goBack(): void {
    this.router.navigate(['/dashboard/turnos']);
  }
}
