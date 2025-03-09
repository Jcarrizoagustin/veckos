import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-usuarios-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule
  ],
  templateUrl: './usuarios-list.component.html',
  styleUrls: ['./usuarios-list.component.css']
})
export class UsuariosListComponent implements OnInit {
  usuarios: any[] = [];
  filteredUsuarios: any[] = [];
  searchTerm: string = '';
  
  displayedColumns: string[] = ['avatar', 'nombre', 'dni', 'edad', 'plan', 'estado', 'fechaInicio', 'acciones'];
  
  constructor(private router: Router) { }
  
  ngOnInit(): void {
    this.loadUsuarios();
  }
  
  loadUsuarios(): void {
    // Simulación de carga de datos desde el backend
    /*
    API endpoint: /api/usuarios
    Estructura esperada:
    [
      {
        "id": 1,
        "nombre": "Tiger",
        "apellido": "Nixon",
        "fechaNacimiento": "1998-05-25",
        "dni": "38552125",
        "cuil": "20-38552125-7",
        "telefono": "3511234567",
        "correo": "tnixon@example.com",
        "estado": "ACTIVO",
        "planActual": "Wellness",
        "fechaInicio": "2022-05-22"
      },
      ...
    ]
    */
    
    this.usuarios = [
      {
        id: 1,
        nombre: 'Tiger',
        apellido: 'Nixon',
        fechaNacimiento: '1998-05-25',
        dni: '38552125',
        edad: 25,
        plan: 'Wellness',
        estado: 'ACTIVO',
        fechaInicio: '22/5/2022'
      },
      {
        id: 2,
        nombre: 'Garrett',
        apellido: 'Winters',
        fechaNacimiento: '2007-05-25',
        dni: '25885963',
        edad: 17,
        plan: 'Sport',
        estado: 'INACTIVO',
        fechaInicio: '22/5/2024'
      },
      {
        id: 3,
        nombre: 'Ashton',
        apellido: 'Cox',
        fechaNacimiento: '1958-05-25',
        dni: '25885963',
        edad: 66,
        plan: 'Wellness',
        estado: 'ACTIVO',
        fechaInicio: '25/5/2024'
      },
      {
        id: 4,
        nombre: 'Tiger',
        apellido: 'Nixon',
        fechaNacimiento: '2001-05-25',
        dni: '25885963',
        edad: 22,
        plan: 'Wellness',
        estado: 'ACTIVO',
        fechaInicio: '22/5/2023'
      },
      {
        id: 5,
        nombre: 'Cedric',
        apellido: 'Kelly',
        fechaNacimiento: '1993-05-25',
        dni: '25885963',
        edad: 31,
        plan: 'Wellness',
        estado: 'ACTIVO',
        fechaInicio: '22/5/2023'
      },
      {
        id: 6,
        nombre: 'Airi',
        apellido: 'Satou',
        fechaNacimiento: '1980-05-25',
        dni: '25885963',
        edad: 45,
        plan: 'Wellness',
        estado: 'INACTIVO',
        fechaInicio: '30/5/2024'
      },
      {
        id: 7,
        nombre: 'Brielle',
        apellido: 'Williamson',
        fechaNacimiento: '2005-05-25',
        dni: '25885963',
        edad: 19,
        plan: 'Wellness',
        estado: 'ACTIVO',
        fechaInicio: '22/5/2022'
      },
      {
        id: 8,
        nombre: 'Herrod',
        apellido: 'Chandler',
        fechaNacimiento: '1963-05-25',
        dni: '25885963',
        edad: 61,
        plan: 'Fitness',
        estado: 'ACTIVO',
        fechaInicio: '28/5/2022'
      },
      {
        id: 9,
        nombre: 'Rhona',
        apellido: 'Davidson',
        fechaNacimiento: '1965-05-25',
        dni: '25885963',
        edad: 59,
        plan: 'Fitness',
        estado: 'INACTIVO',
        fechaInicio: '22/5/2021'
      },
      {
        id: 10,
        nombre: 'Colleen',
        apellido: 'Hurst',
        fechaNacimiento: '1970-05-25',
        dni: '25885963',
        edad: 55,
        plan: 'Sport',
        estado: 'ACTIVO',
        fechaInicio: '21/5/2022'
      },
      {
        id: 11,
        nombre: 'Sonya',
        apellido: 'Frost',
        fechaNacimiento: '1984-05-25',
        dni: '25885963',
        edad: 41,
        plan: 'Sport',
        estado: 'ACTIVO',
        fechaInicio: '22/5/2023'
      },
      {
        id: 12,
        nombre: 'Ashton',
        apellido: 'Cox',
        fechaNacimiento: '1988-05-25',
        dni: '25885963',
        edad: 36,
        plan: 'Fitness',
        estado: 'ACTIVO',
        fechaInicio: '21/5/2023'
      }
    ];
    
    this.filteredUsuarios = [...this.usuarios];
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
  
  viewUsuario(id: number): void {
    this.router.navigate(['/dashboard/usuarios', id]);
  }
  
  editUsuario(id: number): void {
    this.router.navigate(['/dashboard/usuarios/editar', id]);
  }
  
  deleteUsuario(id: number): void {
    if (confirm('¿Está seguro que desea eliminar este usuario?')) {
      // Aquí iría la lógica para eliminar el usuario
      console.log('Eliminando usuario:', id);
      
      // Simulación de eliminación
      this.usuarios = this.usuarios.filter(u => u.id !== id);
      this.filteredUsuarios = this.filteredUsuarios.filter(u => u.id !== id);
    }
  }
  
  createUsuario(): void {
    this.router.navigate(['/dashboard/nuevo-usuario']);
  }
}
