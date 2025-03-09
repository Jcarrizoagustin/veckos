import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { NgxChartsModule } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-usuario-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatTableModule,
    MatChipsModule,
    NgxChartsModule
  ],
  templateUrl: './usuario-detail.component.html',
  styleUrls: ['./usuario-detail.component.css']
})
export class UsuarioDetailComponent implements OnInit {
  usuario: any = null;
  userId: number = 0;
  isLoading: boolean = true;
  
  // Datos para gráficos
  asistenciasChart: any[] = [];
  pagosChart: any[] = [];
  colorScheme: any = {
    domain: ['#6C13F1', '#F44336']
  };
  
  // Datos para tablas
  inscripcionesColumns: string[] = ['fechaInicio', 'fechaFin', 'plan', 'precio', 'estado'];
  inscripciones: any[] = [];
  
  asistenciasColumns: string[] = ['fecha', 'turno', 'presente'];
  asistencias: any[] = [];
  
  pagosColumns: string[] = ['fecha', 'monto', 'metodo', 'descripcion'];
  pagos: any[] = [];
  
  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userId = +params['id']; // Convertir a número
      this.loadUsuarioData();
    });
  }
  
  loadUsuarioData(): void {
    // Simulación de carga de datos desde el backend
    /*
    API endpoint: /api/usuarios/{id}
    Estructura esperada:
    {
      "id": 1,
      "nombre": "Ariel",
      "apellido": "López",
      "fechaNacimiento": "1990-10-26",
      "dni": "33123456",
      "cuil": "20-33123456-0",
      "telefono": "3511234567",
      "correo": "ariel.lopez@example.com",
      "estado": "ACTIVO",
      "fechaAlta": "2022-05-15",
      "inscripcionActiva": {
        "id": 1,
        "fechaInicio": "2025-02-01",
        "fechaFin": "2025-03-01",
        "plan": {
          "id": 1,
          "nombre": "Wellness",
          "precio": 15000
        },
        "estado": "ACTIVO",
        "ultimoPago": "2025-02-01"
      }
    }
    */
    
    setTimeout(() => {
      this.usuario = {
        id: 1,
        nombre: 'Ariel',
        apellido: 'López',
        fechaNacimiento: '1990-10-26',
        edad: 35,
        dni: '33123456',
        cuil: '20-33123456-0',
        telefono: '3511234567',
        correo: 'ariel.lopez@example.com',
        estado: 'ACTIVO',
        fechaAlta: '2022-05-15',
        inscripcionActiva: {
          id: 1,
          fechaInicio: '2025-02-01',
          fechaFin: '2025-03-01',
          proximoPago: '2025-02-22',
          plan: {
            id: 1,
            nombre: 'Wellness',
            precio: 15000
          },
          estado: 'ACTIVO',
          ultimoPago: '2025-02-01'
        },
        estadisticas: {
          totalClases: 10,
          asistencia: 90,
          crecimientoClases: 21.01,
          crecimientoAsistencia: 4.39
        }
      };
      
      this.loadChartData();
      this.loadInscripciones();
      this.loadAsistencias();
      this.loadPagos();
      
      this.isLoading = false;
    }, 1000); // Simular retardo de red
  }
  
  loadChartData(): void {
    /*
    API endpoint: /api/reportes/asistencia?usuarioId={id}
    API endpoint: /api/reportes/financiero?usuarioId={id}
    */
    
    this.asistenciasChart = [
      { name: '25.02', series: [
        { name: 'Income', value: 100000 },
        { name: 'Outcome', value: 80000 }
      ]},
      { name: '26.02', series: [
        { name: 'Income', value: 200000 },
        { name: 'Outcome', value: 70000 }
      ]},
      { name: '27.02', series: [
        { name: 'Income', value: 500000 },
        { name: 'Outcome', value: 60000 }
      ]},
      { name: '28.02', series: [
        { name: 'Income', value: 300000 },
        { name: 'Outcome', value: 100000 }
      ]},
      { name: '29.02', series: [
        { name: 'Income', value: 200000 },
        { name: 'Outcome', value: 60000 }
      ]}
    ];
  }
  
  loadInscripciones(): void {
    /*
    API endpoint: /api/inscripciones/usuario/{id}
    */
    
    this.inscripciones = [
      {
        id: 1,
        fechaInicio: '01/01/2025',
        fechaFin: '31/01/2025',
        plan: 'Wellness',
        precio: '$15.000',
        estado: 'COMPLETADO'
      },
      {
        id: 2,
        fechaInicio: '01/02/2025',
        fechaFin: '28/02/2025',
        plan: 'Wellness',
        precio: '$15.000',
        estado: 'ACTIVO'
      }
    ];
  }
  
  loadAsistencias(): void {
    /*
    API endpoint: /api/asistencias/usuario/{id}
    */
    
    this.asistencias = [
      {
        id: 1,
        fecha: '03/02/2025',
        turno: 'Lunes 7:30',
        presente: true
      },
      {
        id: 2,
        fecha: '05/02/2025',
        turno: 'Miércoles 7:30',
        presente: true
      },
      {
        id: 3,
        fecha: '07/02/2025',
        turno: 'Viernes 7:30',
        presente: false
      },
      {
        id: 4,
        fecha: '10/02/2025',
        turno: 'Lunes 7:30',
        presente: true
      },
      {
        id: 5,
        fecha: '12/02/2025',
        turno: 'Miércoles 7:30',
        presente: true
      }
    ];
  }
  
  loadPagos(): void {
    /*
    API endpoint: /api/pagos/usuario/{id}
    */
    
    this.pagos = [
      {
        id: 1,
        fecha: '01/01/2025',
        monto: '$15.000',
        metodo: 'EFECTIVO',
        descripcion: 'Pago mensualidad Enero'
      },
      {
        id: 2,
        fecha: '01/02/2025',
        monto: '$15.000',
        metodo: 'TRANSFERENCIA',
        descripcion: 'Pago mensualidad Febrero'
      }
    ];
  }
  
  getEstadoClass(estado: string): string {
    switch (estado) {
      case 'ACTIVO': return 'estado-activo';
      case 'INACTIVO': return 'estado-inactivo';
      case 'PENDIENTE': return 'estado-pendiente';
      case 'COMPLETADO': return 'estado-completado';
      case 'PROXIMO_A_VENCER': return 'estado-proximo';
      default: return '';
    }
  }
  
  getAsistenciaClass(presente: boolean): string {
    return presente ? 'asistencia-presente' : 'asistencia-ausente';
  }
  
  editUsuario(): void {
    this.router.navigate(['/dashboard/usuarios/editar', this.userId]);
  }
  
  editPlan(): void {
    this.router.navigate(['/dashboard/inscripciones/editar', this.usuario.inscripcionActiva.id]);
  }
  
  goBack(): void {
    this.router.navigate(['/dashboard/usuarios']);
  }
}
