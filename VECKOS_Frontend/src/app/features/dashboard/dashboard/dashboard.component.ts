import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { NgxChartsModule } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatTabsModule,
    MatIconModule,
    NgxChartsModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  // Datos para el horario
  currentHour: string = '7:30';
  daysOfWeek: string[] = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes'];
  isToday: boolean = true;
  
  // Datos para la asistencia
  scheduleData: any[] = [];
  
  // Datos para estadísticas
  totalActiveUsers: number = 300;
  usersByPlan: any[] = [
    { name: 'Usuarios', value: 110, color: '#6C13F1' },
    { name: 'Fitness', value: 25, color: '#4CAF50' },
    { name: 'Wellness', value: 75, color: '#FF5252' },
    { name: 'Sport', value: 10, color: '#2196F3' }
  ];
  
  // Datos para el gráfico
  chartData: any[] = [];
  
  // Opciones para el gráfico
  colorScheme:any = {
    domain: ['#6C13F1']
  };
  
  constructor() {
    this.generateScheduleData();
    this.generateChartData();
  }
  
  ngOnInit(): void {
    // Simulación de carga de datos del backend
  }
  
  generateScheduleData(): void {
    // Datos de ejemplo para el horario
    // Esta información vendría del backend
    /*
    API endpoint: /api/clases/fecha?fecha=2025-03-10
    Estructura esperada:
    [
      {
        "id": 1,
        "turnoId": 1,
        "fecha": "2025-03-10",
        "diaSemana": "MONDAY",
        "hora": "07:30:00",
        "usuarios": [
          {"id": 1, "nombre": "Luciana", "apellido": "Lopez", "estado": "ACTIVO"},
          ...
        ]
      },
      ...
    ]
    */
    
    this.scheduleData = [
      {
        day: 'Lunes',
        users: [
          { id: 1, fullName: 'Luciana Lopez', status: 'ACTIVO' },
          { id: 2, fullName: 'Lautaro Ruíñez', status: 'ACTIVO' },
          { id: 3, fullName: 'Flor Acosta', status: 'ACTIVO' },
          { id: 4, fullName: 'Andrea Quiroga', status: 'INACTIVO' },
          { id: 5, fullName: 'Carla Sabina', status: 'RESERVADO' },
          { id: 6, fullName: 'Samuel Cordoba', status: 'ACTIVO' }
        ]
      },
      {
        day: 'Martes',
        users: [
          { id: 1, fullName: 'Luciana Lopez', status: 'ACTIVO' },
          { id: 7, fullName: 'Daniel Parra', status: 'PROXIMO_A_VENCER' },
          { id: 3, fullName: 'Flor Acosta', status: 'ACTIVO' },
          { id: 4, fullName: 'Andrea Quiroga', status: 'INACTIVO' },
          { id: 5, fullName: 'Carla Sabina', status: 'RESERVADO' }
        ]
      },
      {
        day: 'Miércoles',
        users: [
          { id: 1, fullName: 'Luciana Lopez', status: 'ACTIVO' },
          { id: 2, fullName: 'Lautaro Ruíñez', status: 'ACTIVO' },
          { id: 3, fullName: 'Flor Acosta', status: 'ACTIVO' },
          { id: 4, fullName: 'Andrea Quiroga', status: 'INACTIVO' },
          { id: 5, fullName: 'Carla Sabina', status: 'RESERVADO' },
          { id: 6, fullName: 'Samuel Cordoba', status: 'ACTIVO' }
        ]
      },
      {
        day: 'Jueves',
        users: [
          { id: 1, fullName: 'Luciana Lopez', status: 'ACTIVO' },
          { id: 7, fullName: 'Daniel Parra', status: 'PROXIMO_A_VENCER' },
          { id: 3, fullName: 'Flor Acosta', status: 'ACTIVO' },
          { id: 4, fullName: 'Andrea Quiroga', status: 'INACTIVO' },
          { id: 5, fullName: 'Carla Sabina', status: 'RESERVADO' }
        ]
      },
      {
        day: 'Viernes',
        users: [
          { id: 1, fullName: 'Luciana Lopez', status: 'ACTIVO' },
          { id: 2, fullName: 'Lautaro Ruíñez', status: 'ACTIVO' },
          { id: 3, fullName: 'Flor Acosta', status: 'ACTIVO' },
          { id: 4, fullName: 'Andrea Quiroga', status: 'INACTIVO' },
          { id: 5, fullName: 'Carla Sabina', status: 'RESERVADO' },
          { id: 6, fullName: 'Samuel Cordoba', status: 'ACTIVO' }
        ]
      }
    ];
  }
  
  generateChartData(): void {
    // Datos de ejemplo para el gráfico
    // Esta información vendría del backend
    /*
    API endpoint: /api/reportes/dashboard/stats
    Estructura esperada:
    {
      "usuariosActivos": 300,
      "ingresosMesActual": 150000,
      "ingresosMesAnterior": 145000,
      "asistenciasHoy": 45,
      "asistenciasUltimaSemana": [...]
    }
    */
    
    this.chartData = [
      { name: '01/03', value: 200 },
      { name: '02/03', value: 300 },
      { name: '03/03', value: 250 },
      { name: '04/03', value: 280 },
      { name: '05/03', value: 180 },
      { name: '06/03', value: 270 },
      { name: '07/03', value: 250 },
      { name: '08/03', value: 300 },
      { name: '09/03', value: 215 }
    ];
  }
  
  getUserStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVO': return 'user-active';
      case 'INACTIVO': return 'user-inactive';
      case 'PROXIMO_A_VENCER': return 'user-expiring';
      case 'RESERVADO': return 'user-reserved';
      default: return '';
    }
  }
  
  previousHour(): void {
    // Lógica para cambiar a la hora anterior
    console.log('Hora anterior');
  }
  
  nextHour(): void {
    // Lógica para cambiar a la hora siguiente
    console.log('Hora siguiente');
  }
  
  addUser(day: string): void {
    // Lógica para agregar un usuario
    console.log('Agregar usuario al día:', day);
  }
}