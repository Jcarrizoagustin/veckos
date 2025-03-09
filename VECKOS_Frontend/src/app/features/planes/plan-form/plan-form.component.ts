import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-plan-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './plan-form.component.html',
  styleUrls: ['./plan-form.component.css']
})
export class PlanFormComponent implements OnInit {
  planForm: FormGroup;
  isEditMode = false;
  planId: number | null = null;
  isLoading = false;
  isSaving = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.planForm = this.fb.group({
      nombre: ['', [Validators.required]],
      precio: ['', [Validators.required, Validators.min(0)]],
      descripcion: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.planId = +params['id'];
        this.loadPlanData();
      }
    });
  }

  loadPlanData(): void {
    this.isLoading = true;
    
    // Simulación de carga de datos desde el backend
    /*
    API endpoint: /api/planes/{id}
    Estructura esperada:
    {
      "id": 1,
      "nombre": "Wellness",
      "precio": 15000,
      "descripcion": "Plan básico de entrenamiento"
    }
    */
    
    setTimeout(() => {
      const plan = {
        id: this.planId,
        nombre: 'Wellness',
        precio: 15000,
        descripcion: 'Plan básico de entrenamiento'
      };
      
      this.planForm.patchValue({
        nombre: plan.nombre,
        precio: plan.precio,
        descripcion: plan.descripcion
      });
      
      this.isLoading = false;
    }, 1000);
  }

  onSubmit(): void {
    if (this.planForm.invalid) {
      return;
    }
    
    this.isSaving = true;
    
    const planData = {
      ...this.planForm.value,
      id: this.isEditMode ? this.planId : null
    };
    
    // Simulación de envío al backend
    /*
    API endpoint: POST /api/planes (crear)
    API endpoint: PUT /api/planes/{id} (actualizar)
    */
    
    setTimeout(() => {
      console.log('Datos del plan enviados:', planData);
      
      this.isSaving = false;
      
      this.snackBar.open(
        this.isEditMode ? 'Plan actualizado exitosamente' : 'Plan creado exitosamente',
        'Cerrar',
        { duration: 3000 }
      );
      
      this.router.navigate(['/dashboard/planes']);
    }, 1000);
  }

  cancel(): void {
    this.router.navigate(['/dashboard/planes']);
  }
}
