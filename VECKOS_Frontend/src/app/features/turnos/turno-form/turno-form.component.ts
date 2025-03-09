// src/app/features/turnos/turno-form/turno-form.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-turno-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './turno-form.component.html',
  styleUrls: ['./turno-form.component.css']
})
export class TurnoFormComponent implements OnInit {
  turnoForm: FormGroup;
  isEditMode = false;
  turnoId: number | null = null;
  isLoading = false;
  isSaving = false;
  
  diasSemana = [
    { nombre: 'Lunes', value: 'MONDAY' },
    { nombre: 'Martes', value: 'TUESDAY' },
    { nombre: 'Miércoles', value: 'WEDNESDAY' },
    { nombre: 'Jueves', value: 'THURSDAY' },
    { nombre: 'Viernes', value: 'FRIDAY' },
    { nombre: 'Sábado', value: 'SATURDAY' },
    { nombre: 'Domingo', value: 'SUNDAY' }
  ];
  
  horas = Array.from({ length: 24 }, (_, i) => {
    const hour = i.toString().padStart(2, '0');
    return [
      { label: `${hour}:00`, value: `${hour}:00:00` },
      { label: `${hour}:30`, value: `${hour}:30:00` }
    ];
  }).flat();

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.turnoForm = this.fb.group({
      diaSemana: ['', [Validators.required]],
      hora: ['', [Validators.required]],
      descripcion: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.turnoId = +params['id'];
        this.loadTurnoData();
      }
    });
  }

  loadTurnoData(): void {
    this.isLoading = true;
    
    // Simulación de carga de datos desde el backend
    /*
    API endpoint: /api/turnos/{id}
    Estructura esperada:
    {
      "id": 1,
      "diaSemana": "MONDAY",
      "hora": "07:30:00",
      "descripcion": "Turno mañana"
    }
    */
    
    setTimeout(() => {
      const turno = {
        id: this.turnoId,
        diaSemana: 'MONDAY',
        hora: '07:30:00',
        descripcion: 'Turno mañana'
      };
      
      this.turnoForm.patchValue({
        diaSemana: turno.diaSemana,
        hora: turno.hora,
        descripcion: turno.descripcion
      });
      
      this.isLoading = false;
    }, 1000);
  }

  onSubmit(): void {
    if (this.turnoForm.invalid) {
      return;
    }
    
    this.isSaving = true;
    
    const turnoData = {
      ...this.turnoForm.value,
      id: this.isEditMode ? this.turnoId : null
    };
    
    // Simulación de envío al backend
    /*
    API endpoint: POST /api/turnos (crear)
    API endpoint: PUT /api/turnos/{id} (actualizar)
    */
    
    setTimeout(() => {
      console.log('Datos del turno enviados:', turnoData);
      
      this.isSaving = false;
      
      this.snackBar.open(
        this.isEditMode ? 'Turno actualizado exitosamente' : 'Turno creado exitosamente',
        'Cerrar',
        { duration: 3000 }
      );
      
      this.router.navigate(['/dashboard/turnos']);
    }, 1000);
  }

  cancel(): void {
    this.router.navigate(['/dashboard/turnos']);
  }
  
  getDiaSemana(value: string): string {
    const dia = this.diasSemana.find(d => d.value === value);
    return dia ? dia.nombre : '';
  }
}
