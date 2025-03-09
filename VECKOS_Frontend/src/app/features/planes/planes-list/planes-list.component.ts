import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-planes-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatDividerModule,
    MatSnackBarModule
  ],
  templateUrl: './planes-list.component.html',
  styleUrls: ['./planes-list.component.css']
})
export class PlanesListComponent implements OnInit {
  planes: any[] = [];
  isLoading = true;
  displayedColumns: string[] = ['nombre', 'precio', 'descripcion', 'usuariosActivos', 'acciones'];

  constructor(
    private router: Router,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadPlanes();
  }

  loadPlanes(): void {
    // Simulación de carga de datos desde el backend
    /*
    API endpoint: /api/planes
    Estructura esperada:
    [
      {
        "id": 1,
        "nombre": "Wellness",
        "precio": 15000,
        "descripcion": "Plan básico de entrenamiento",
        "usuariosActivos": 110
      },
      ...
    ]
    */
    
    setTimeout(() => {
      this.planes = [
        { 
          id: 1, 
          nombre: 'Wellness', 
          precio: 15000, 
          descripcion: 'Plan básico de entrenamiento',
          usuariosActivos: 75
        },
        { 
          id: 2, 
          nombre: 'Fitness', 
          precio: 18000, 
          descripcion: 'Plan intermedio con acceso a todas las máquinas',
          usuariosActivos: 25
        },
        { 
          id: 3, 
          nombre: 'Sport', 
          precio: 22000, 
          descripcion: 'Plan premium con acceso a clases especiales',
          usuariosActivos: 10
        }
      ];
      
      this.isLoading = false;
    }, 1000);
  }

  createPlan(): void {
    this.router.navigate(['/dashboard/planes/nuevo']);
  }

  editPlan(id: number): void {
    this.router.navigate(['/dashboard/planes/editar', id]);
  }

  deletePlan(id: number): void {
    if (confirm('¿Está seguro que desea eliminar este plan?')) {
      // Aquí iría la lógica para eliminar el plan
      // API endpoint: DELETE /api/planes/{id}
      
      // Simulamos la eliminación
      this.planes = this.planes.filter(plan => plan.id !== id);
      
      this.snackBar.open('Plan eliminado exitosamente', 'Cerrar', {
        duration: 3000
      });
    }
  }

  formatPrice(price: number): string {
    return `$${price.toLocaleString('es-AR')}`;
  }
}
