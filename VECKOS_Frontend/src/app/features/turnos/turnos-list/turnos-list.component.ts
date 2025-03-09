// src/app/features/turnos/turnos-list/turnos-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDividerModule } from '@angular/material/divider';
import { MatBadgeModule } from '@angular/material/badge';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-turnos-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatTabsModule,
    MatDividerModule,
    MatBadgeModule,
    MatSnackBarModule
  ],
  templateUrl: './turnos-list.component.html',
  styleUrls: ['./turnos-list.component.css']
})
export class TurnosListComponent implements OnInit {
  diasSemana: any[] = [
    { nombre: 'Lunes', value: 'MONDAY', turnos: [] },
    { nombre: 'Martes', value: 'TUESDAY', turnos: [] },
    { nombre: 'Miércoles', value: 'WEDNESDAY', turnos: [] },
    { nombre: 'Jueves', value: 'THURSDAY', turnos: [] },
    { nombre: 'Viernes', value: 'FRIDAY', turnos: [] },
    { nombre: 'Sábado', value: 'SATURDAY', turnos: [] },
    { nombre: 'Domingo', value: 'SUNDAY', turnos: [] }
  ];
  
  isLoading = true;
  activeTabIndex = 0; // Por defecto, Lunes

  constructor(
    private router: Router,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadTurnos();
  }

  loadTurnos(): void {
    // Simulación de carga de datos desde el backend
    /*
    API endpoint: /api/turnos
    Estructura esperada:
    [
      {
        "id": 1,
        "diaSemana": "MONDAY",
        "hora": "07:30:00",
        "descripcion": "Turno mañana",
        "usuariosInscritos": 15
      },
      ...
    ]
    */
    
    setTimeout(() => {
      // Datos de ejemplo
      const turnosData = [
        { id: 1, diaSemana: 'MONDAY', hora: '07:30:00', descripcion: 'Turno mañana', usuariosInscritos: 15 },
        { id: 2, diaSemana: 'MONDAY', hora: '10:30:00', descripcion: 'Turno media mañana', usuariosInscritos: 8 },
        { id: 3, diaSemana: 'MONDAY', hora: '19:00:00', descripcion: 'Turno noche', usuariosInscritos: 12 },
        { id: 4, diaSemana: 'TUESDAY', hora: '08:00:00', descripcion: 'Turno mañana', usuariosInscritos: 10 },
        { id: 5, diaSemana: 'TUESDAY', hora: '18:00:00', descripcion: 'Turno tarde', usuariosInscritos: 14 },
        { id: 6, diaSemana: 'WEDNESDAY', hora: '07:30:00', descripcion: 'Turno mañana', usuariosInscritos: 9 },
        { id: 7, diaSemana: 'WEDNESDAY', hora: '16:00:00', descripcion: 'Turno tarde', usuariosInscritos: 11 },
        { id: 8, diaSemana: 'THURSDAY', hora: '09:00:00', descripcion: 'Turno mañana', usuariosInscritos: 7 },
        { id: 9, diaSemana: 'THURSDAY', hora: '19:30:00', descripcion: 'Turno noche', usuariosInscritos: 15 },
        { id: 10, diaSemana: 'FRIDAY', hora: '07:30:00', descripcion: 'Turno mañana', usuariosInscritos: 13 },
        { id: 11, diaSemana: 'FRIDAY', hora: '15:00:00', descripcion: 'Turno tarde', usuariosInscritos: 6 }
      ];
      
      // Organizar los turnos por día de la semana
      this.diasSemana.forEach(dia => {
        dia.turnos = turnosData.filter(turno => turno.diaSemana === dia.value)
          .sort((a, b) => a.hora.localeCompare(b.hora));
      });
      
      this.isLoading = false;
    }, 1000);
  }

  createTurno(): void {
    this.router.navigate(['/dashboard/turnos/nuevo']);
  }

  editTurno(id: number): void {
    this.router.navigate(['/dashboard/turnos/editar', id]);
  }

  deleteTurno(id: number): void {
    if (confirm('¿Está seguro que desea eliminar este turno?')) {
      // Aquí iría la lógica para eliminar el turno
      // API endpoint: DELETE /api/turnos/{id}
      
      // Simulamos la eliminación
      this.diasSemana.forEach(dia => {
        dia.turnos = dia.turnos.filter((turno: any) => turno.id !== id);
      });
      
      this.snackBar.open('Turno eliminado exitosamente', 'Cerrar', {
        duration: 3000
      });
    }
  }

  viewUsuarios(id: number): void {
    this.router.navigate(['/dashboard/turnos/usuarios', id]);
  }

  formatHora(hora: string): string {
    // Convertir formato de 24 horas a formato de 12 horas
    const [hours, minutes] = hora.split(':');
    const hoursNum = parseInt(hours);
    const period = hoursNum >= 12 ? 'PM' : 'AM';
    const hours12 = hoursNum % 12 || 12;
    return `${hours12}:${minutes} ${period}`;
  }
}
