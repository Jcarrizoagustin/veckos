import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatStepperModule } from '@angular/material/stepper';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatSnackBarModule,
    MatIconModule,
    MatStepperModule,
    MatCardModule
  ],
  templateUrl: './usuario-form.component.html',
  styleUrls: ['./usuario-form.component.css']
})
export class UsuarioFormComponent implements OnInit {
  usuarioForm: FormGroup;
  planForm: FormGroup;
  confirmacionForm: FormGroup;
  isEditMode: boolean = false;
  userId: number | null = null;
  isLoading: boolean = false;
  
  // Datos para los selectores
  planes: any[] = [];
  frecuencias: any[] = [
    { value: 3, label: '3 días a la semana' },
    { value: 5, label: '5 días a la semana' }
  ];
  
  // Estado de los pasos
  pasoActual: number = 0;
  
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.usuarioForm = this.fb.group({
      nombre: ['', [Validators.required]],
      apellido: ['', [Validators.required]],
      dni: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      cuil: ['', [Validators.pattern(/^\d{2}-\d{8}-\d{1}$/)]],
      fechaNacimiento: ['', [Validators.required]],
      telefono: ['', [Validators.pattern(/^\d{10}$/)]],
      correo: ['', [Validators.email]]
    });
    
    this.planForm = this.fb.group({
      planId: ['', [Validators.required]],
      frecuencia: [3, [Validators.required]],
      fechaInicio: [new Date(), [Validators.required]]
    });
    
    this.confirmacionForm = this.fb.group({
      confirmado: [false, [Validators.requiredTrue]]
    });
  }
  
  ngOnInit(): void {
    this.loadPlanes();
    
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.userId = +params['id'];
        this.loadUsuarioData();
      }
    });
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
        "descripcion": "Plan básico"
      },
      ...
    ]
    */
    
    this.planes = [
      { id: 1, nombre: 'Wellness', precio: 15000, descripcion: 'Plan básico de entrenamiento' },
      { id: 2, nombre: 'Fitness', precio: 18000, descripcion: 'Plan intermedio con acceso a todas las máquinas' },
      { id: 3, nombre: 'Sport', precio: 22000, descripcion: 'Plan premium con acceso a clases especiales' }
    ];
  }
  
  loadUsuarioData(): void {
    // Simulación de carga de datos desde el backend
    /*
    API endpoint: /api/usuarios/{id}
    Estructura esperada: Ver en el componente de detalle
    */
    
    this.isLoading = true;
    
    setTimeout(() => {
      // Simulación de respuesta del backend
      const usuario = {
        id: this.userId,
        nombre: 'Ariel',
        apellido: 'López',
        fechaNacimiento: '1990-10-26',
        dni: '33123456',
        cuil: '20-33123456-0',
        telefono: '3511234567',
        correo: 'ariel.lopez@example.com',
        estado: 'ACTIVO',
        inscripcionActiva: {
          id: 1,
          planId: 1,
          frecuencia: 3,
          fechaInicio: '2025-02-01'
        }
      };
      
      // Rellenar el formulario con los datos del usuario
      this.usuarioForm.patchValue({
        nombre: usuario.nombre,
        apellido: usuario.apellido,
        dni: usuario.dni,
        cuil: usuario.cuil,
        fechaNacimiento: new Date(usuario.fechaNacimiento),
        telefono: usuario.telefono,
        correo: usuario.correo
      });
      
      // Si estamos en modo edición, no necesitamos el paso de plan
      if (this.isEditMode) {
        this.planForm.patchValue({
          planId: usuario.inscripcionActiva.planId,
          frecuencia: usuario.inscripcionActiva.frecuencia,
          fechaInicio: new Date(usuario.inscripcionActiva.fechaInicio)
        });
      }
      
      this.isLoading = false;
    }, 1000);
  }
  
  onSubmit(): void {
    if (this.usuarioForm.invalid || (!this.isEditMode && this.planForm.invalid)) {
      return;
    }
    
    this.isLoading = true;
    
    const formData = {
      ...this.usuarioForm.value,
      ...(this.isEditMode ? {} : { plan: this.planForm.value })
    };
    
    // Simulación de envío al backend
    setTimeout(() => {
      console.log('Datos enviados:', formData);
      
      // Simulación de respuesta exitosa
      this.isLoading = false;
      
      this.snackBar.open(
        this.isEditMode 
          ? 'Usuario actualizado exitosamente' 
          : 'Usuario creado exitosamente',
        'Cerrar',
        { duration: 3000 }
      );
      
      // Redirección
      if (this.isEditMode) {
        this.router.navigate(['/dashboard/usuarios', this.userId]);
      } else {
        this.router.navigate(['/dashboard/usuarios']);
      }
    }, 1500);
  }
  
  nextStep(): void {
    if (this.pasoActual === 0 && this.usuarioForm.invalid) {
      this.markFormGroupTouched(this.usuarioForm);
      return;
    }
    
    if (this.pasoActual === 1 && this.planForm.invalid) {
      this.markFormGroupTouched(this.planForm);
      return;
    }
    
    if (this.pasoActual < 2) {
      this.pasoActual++;
    }
  }
  
  prevStep(): void {
    if (this.pasoActual > 0) {
      this.pasoActual--;
    }
  }
  
  markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
  
  getFormData(): any {
    const usuarioData = this.usuarioForm.value;
    const planData = this.planForm.value;
    
    return {
      usuario: {
        nombre: usuarioData.nombre,
        apellido: usuarioData.apellido,
        dni: usuarioData.dni,
        cuil: usuarioData.cuil,
        fechaNacimiento: usuarioData.fechaNacimiento,
        telefono: usuarioData.telefono,
        correo: usuarioData.correo
      },
      plan: {
        nombre: this.planes.find(p => p.id === planData.planId)?.nombre || '',
        frecuencia: planData.frecuencia,
        fechaInicio: planData.fechaInicio
      }
    };
  }
  
  cancel(): void {
    if (this.isEditMode) {
      this.router.navigate(['/dashboard/usuarios', this.userId]);
    } else {
      this.router.navigate(['/dashboard/usuarios']);
    }
  }

  getPlanNombre(planId: number | null): string {
    if (!planId) return '';
    const plan = this.planes.find(p => p.id === planId);
    return plan ? plan.nombre : '';
  }
}
