// src/app/features/inscripciones/inscripcion-form/inscripcion-form.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormArray, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatStepperModule } from '@angular/material/stepper';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRadioModule } from '@angular/material/radio';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { Observable, of } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-inscripcion-form',
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
    MatSnackBarModule,
    MatStepperModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCheckboxModule,
    MatRadioModule,
    MatTooltipModule,
    MatChipsModule,
    MatDividerModule,
    MatAutocompleteModule
  ],
  templateUrl: './inscripcion-form.component.html',
  styleUrls: ['./inscripcion-form.component.css']
})
export class InscripcionFormComponent implements OnInit {
  isLoading: boolean = false;
  isEditMode: boolean = false;
  isRenovacionMode: boolean = false;
  inscripcionId: number | null = null;
  usuarioId: number | null = null;
  
  // Formularios para cada paso
  usuarioForm: FormGroup;
  planForm: FormGroup;
  turnosForm: FormGroup;
  pagoForm: FormGroup;
  
  // Estado de los pasos
  pasoActual: number = 0;
  pasosLabels: string[] = ['Usuario', 'Plan', 'Turnos', 'Pago', 'Confirmación'];
  
  // Datos de selección
  usuarios: any[] = [];
  filteredUsuarios: Observable<any[]> = of([]);
  planes: any[] = [];
  frecuencias: any[] = [
    { valor: 3, etiqueta: '3 veces por semana' },
    { valor: 5, etiqueta: '5 veces por semana' }
  ];
  turnos: any[] = [];
  diasSemana: any[] = [
    { valor: 'MONDAY', nombre: 'Lunes' },
    { valor: 'TUESDAY', nombre: 'Martes' },
    { valor: 'WEDNESDAY', nombre: 'Miércoles' },
    { valor: 'THURSDAY', nombre: 'Jueves' },
    { valor: 'FRIDAY', nombre: 'Viernes' },
    { valor: 'SATURDAY', nombre: 'Sábado' },
    { valor: 'SUNDAY', nombre: 'Domingo' }
  ];
  metodosPago: any[] = [
    { valor: 'EFECTIVO', nombre: 'Efectivo' },
    { valor: 'TRANSFERENCIA', nombre: 'Transferencia bancaria' },
    { valor: 'TARJETA_DEBITO', nombre: 'Tarjeta de débito' },
    { valor: 'TARJETA_CREDITO', nombre: 'Tarjeta de crédito' },
    { valor: 'OTRO', nombre: 'Otro método' }
  ];
  
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    // Inicializar formularios
    this.usuarioForm = this.fb.group({
      usuarioId: ['', Validators.required],
      usuarioNombre: [''],
      usuarioBusqueda: ['']
    });
    
    this.planForm = this.fb.group({
      planId: ['', Validators.required],
      frecuencia: [3, Validators.required],
      fechaInicio: [new Date(), Validators.required],
      fechaFin: [{value: '', disabled: true}]
    });
    
    this.turnosForm = this.fb.group({
      diasSeleccionados: [[], Validators.required],
      turnos: this.fb.array([])
    });
    
    this.pagoForm = this.fb.group({
      monto: ['', [Validators.required, Validators.min(0)]],
      metodoPago: ['EFECTIVO', Validators.required],
      fechaPago: [new Date(), Validators.required],
      descripcion: [''],
      registrarPago: [true]
    });
    
    // Inicializar observable para autocompletado
    const usuarioBusquedaControl = this.usuarioForm.get('usuarioBusqueda');
this.filteredUsuarios = usuarioBusquedaControl 
  ? usuarioBusquedaControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filterUsuarios(value || ''))
    )
  : of([]);
  }
  
  ngOnInit(): void {
    this.loadInitialData();
    
    // Escuchar cambios en la ruta
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.inscripcionId = +params['id'];
        this.isEditMode = true;
        this.loadInscripcionData();
      }
      
      if (params['usuarioId']) {
        this.usuarioId = +params['usuarioId'];
        this.loadUsuarioData();
      }
      
      if (params['renovar'] === 'true' && params['id']) {
        this.isRenovacionMode = true;
      }
    });
    
    // Actualizar fecha de fin cuando cambia la fecha de inicio
    const fechaInicioControl = this.planForm.get('fechaInicio');
    if (fechaInicioControl) {
      fechaInicioControl.valueChanges.subscribe(value => {
        if (value) {
          const fechaInicio = new Date(value);
          const fechaFin = new Date(fechaInicio);
          fechaFin.setMonth(fechaFin.getMonth() + 1);
          
          const fechaFinControl = this.planForm.get('fechaFin');
          if (fechaFinControl) {
            fechaFinControl.setValue(fechaFin);
          }
        }
      });
    }
    
    // Reiniciar selección de turnos cuando cambia la frecuencia
    const frecuenciaControl = this.planForm.get('frecuencia');
    if (frecuenciaControl) {
      frecuenciaControl.valueChanges.subscribe(() => {
        const diasSeleccionadosControl = this.turnosForm.get('diasSeleccionados');
        if (diasSeleccionadosControl) {
          diasSeleccionadosControl.setValue([]);
        }
      });
    }
  }
  
  loadInitialData(): void {
    this.isLoading = true;
    
    // Cargar datos de usuarios
    /*
    API endpoint: /api/usuarios
    Estructura esperada: Lista de usuarios con id, nombre, apellido, etc.
    */
    setTimeout(() => {
      this.usuarios = [
        { id: 1, nombre: 'Luciana', apellido: 'Lopez', dni: '33123456', estado: 'ACTIVO' },
        { id: 2, nombre: 'Lautaro', apellido: 'Ruíñez', dni: '33654987', estado: 'ACTIVO' },
        { id: 3, nombre: 'Flor', apellido: 'Acosta', dni: '37765432', estado: 'ACTIVO' },
        { id: 4, nombre: 'Andrea', apellido: 'Quiroga', dni: '32145678', estado: 'INACTIVO' },
        { id: 5, nombre: 'Carla', apellido: 'Sabina', dni: '35789654', estado: 'ACTIVO' },
        { id: 6, nombre: 'Samuel', apellido: 'Cordoba', dni: '33987654', estado: 'ACTIVO' },
        { id: 7, nombre: 'Ariel', apellido: 'López', dni: '33123456', estado: 'ACTIVO' }
      ];
      
      // Cargar datos de planes
      /*
      API endpoint: /api/planes
      Estructura esperada: Lista de planes con id, nombre, precio, etc.
      */
      this.planes = [
        { id: 1, nombre: 'Wellness', precio: 15000, descripcion: 'Plan básico de entrenamiento' },
        { id: 2, nombre: 'Fitness', precio: 18000, descripcion: 'Plan intermedio con acceso a todas las máquinas' },
        { id: 3, nombre: 'Sport', precio: 22000, descripcion: 'Plan premium con acceso a clases especiales' }
      ];
      
      // Cargar datos de turnos
      /*
      API endpoint: /api/turnos
      Estructura esperada: Lista de turnos con id, diaSemana, hora, etc.
      */
      this.turnos = [
        { id: 1, diaSemana: 'MONDAY', hora: '07:30:00', descripcion: 'Turno mañana' },
        { id: 2, diaSemana: 'MONDAY', hora: '10:30:00', descripcion: 'Turno media mañana' },
        { id: 3, diaSemana: 'MONDAY', hora: '19:00:00', descripcion: 'Turno noche' },
        { id: 4, diaSemana: 'TUESDAY', hora: '08:00:00', descripcion: 'Turno mañana' },
        { id: 5, diaSemana: 'TUESDAY', hora: '18:00:00', descripcion: 'Turno tarde' },
        { id: 6, diaSemana: 'WEDNESDAY', hora: '07:30:00', descripcion: 'Turno mañana' },
        { id: 7, diaSemana: 'WEDNESDAY', hora: '16:00:00', descripcion: 'Turno tarde' },
        { id: 8, diaSemana: 'THURSDAY', hora: '09:00:00', descripcion: 'Turno mañana' },
        { id: 9, diaSemana: 'THURSDAY', hora: '19:30:00', descripcion: 'Turno noche' },
        { id: 10, diaSemana: 'FRIDAY', hora: '07:30:00', descripcion: 'Turno mañana' },
        { id: 11, diaSemana: 'FRIDAY', hora: '15:00:00', descripcion: 'Turno tarde' }
      ];
      
      this.isLoading = false;
    }, 1000);
  }

  get usuarioBusquedaControl(): FormControl {
    return this.usuarioForm.get('usuarioBusqueda') as FormControl;
  }
  
  loadUsuarioData(): void {
    if (!this.usuarioId) return;
    
    // Buscar usuario en la lista
    const usuario = this.usuarios.find(u => u.id === this.usuarioId);
    if (usuario) {
      this.usuarioForm.patchValue({
        usuarioId: usuario.id,
        usuarioNombre: `${usuario.nombre} ${usuario.apellido}`,
        usuarioBusqueda: `${usuario.nombre} ${usuario.apellido}`
      });
      
      // Avanzar al siguiente paso
      this.pasoActual = 1;
    }
  }
  
  loadInscripcionData(): void {
    if (!this.inscripcionId) return;
    
    this.isLoading = true;
    
    // Cargar datos de la inscripción
    /*
    API endpoint: /api/inscripciones/{id}
    Estructura esperada: Datos de la inscripción con usuario, plan, frecuencia, fechas, etc.
    */
    setTimeout(() => {
      const inscripcion = {
        id: this.inscripcionId,
        usuario: {
          id: 1,
          nombre: 'Luciana',
          apellido: 'Lopez',
          dni: '33123456'
        },
        plan: {
          id: 1,
          nombre: 'Wellness',
          precio: 15000
        },
        frecuencia: 3,
        fechaInicio: new Date('2025-02-01'),
        fechaFin: new Date('2025-03-01'),
        estado: 'ACTIVO',
        detalles: [
          { diaSemana: 'MONDAY', turnoId: 1 },
          { diaSemana: 'WEDNESDAY', turnoId: 6 },
          { diaSemana: 'FRIDAY', turnoId: 10 }
        ]
      };
      
      // Cargar datos en los formularios
      this.usuarioForm.patchValue({
        usuarioId: inscripcion.usuario.id,
        usuarioNombre: `${inscripcion.usuario.nombre} ${inscripcion.usuario.apellido}`,
        usuarioBusqueda: `${inscripcion.usuario.nombre} ${inscripcion.usuario.apellido}`
      });
      
      this.planForm.patchValue({
        planId: inscripcion.plan.id,
        frecuencia: inscripcion.frecuencia,
        fechaInicio: inscripcion.fechaInicio
      });
      
      // Si es modo renovación, establecer fecha de inicio después de la fecha de fin
      if (this.isRenovacionMode) {
        const nuevaFechaInicio = new Date(inscripcion.fechaFin);
        nuevaFechaInicio.setDate(nuevaFechaInicio.getDate() + 1);
        this.planForm.patchValue({
          fechaInicio: nuevaFechaInicio
        });
      }
      
      // Cargar selección de días y turnos
      const diasSeleccionados = inscripcion.detalles.map(d => d.diaSemana);
      this.turnosForm.patchValue({
        diasSeleccionados: diasSeleccionados
      });
      
      // Calcular monto a pagar
      if (this.isRenovacionMode || !this.isEditMode) {
        const plan = this.planes.find(p => p.id === inscripcion.plan.id);
        if (plan) {
          this.pagoForm.patchValue({
            monto: plan.precio
          });
        }
      }
      
      this.isLoading = false;
    }, 1500);
  }
  
  // Filtro para el autocompletado de usuarios
  private _filterUsuarios(value: string): any[] {
    const filterValue = value.toLowerCase();
    return this.usuarios.filter(usuario => 
      `${usuario.nombre} ${usuario.apellido}`.toLowerCase().includes(filterValue) ||
      usuario.dni.includes(filterValue)
    );
  }
  
  // Manejar selección de usuario en el autocompletado
  onUsuarioSelected(usuario: any): void {
    this.usuarioForm.patchValue({
      usuarioId: usuario.id,
      usuarioNombre: `${usuario.nombre} ${usuario.apellido}`
    });
  }
  
  // Obtener lista de turnos disponibles para un día de la semana
  getTurnosByDia(dia: string): any[] {
    return this.turnos.filter(turno => turno.diaSemana === dia);
  }
  
  // Formatear hora para mostrar
  formatHora(hora: string): string {
    if (!hora) return '';
    const [hours, minutes] = hora.split(':');
    const hoursNum = parseInt(hours);
    const period = hoursNum >= 12 ? 'PM' : 'AM';
    const hours12 = hoursNum % 12 || 12;
    return `${hours12}:${minutes} ${period}`;
  }
  
  // Chequear si un día está seleccionado
  isDiaSelected(dia: string): boolean {
    const diasSeleccionados = this.turnosForm.get('diasSeleccionados')?.value;
    return diasSeleccionados ? diasSeleccionados.includes(dia) : false;
  }
  
  // Manejar selección/deselección de día de la semana
  toggleDiaSeleccion(dia: string): void {
    const diasSeleccionadosControl = this.turnosForm.get('diasSeleccionados');
    if (!diasSeleccionadosControl) return;
    
    const diasSeleccionados = ["",""];
    const index = diasSeleccionados.indexOf(dia);
    
    // Si ya está seleccionado, removerlo
    if (index !== -1) {
      diasSeleccionados.splice(index, 1);
    } 
    // Si no está seleccionado, agregarlo (verificando límite según frecuencia)
    else {
      const frecuenciaControl = this.planForm.get('frecuencia');
      const frecuencia = frecuenciaControl ? frecuenciaControl.value : 3;
      
      if (diasSeleccionados.length < frecuencia) {
        diasSeleccionados.push(dia);
      } else {
        this.snackBar.open(`Solo puede seleccionar ${frecuencia} días para esta frecuencia`, 'Cerrar', {
          duration: 3000
        });
        return;
      }
    }
    
    diasSeleccionadosControl.setValue(diasSeleccionados);
  }
  
  // Manejar selección de turno para un día
  seleccionarTurno(dia: string, turnoId: number): void {
    console.log(`Turno seleccionado para ${dia}: ${turnoId}`);
    // Aquí implementaríamos la lógica para guardar el turno seleccionado
  }
  
  // Cambiar entre pasos del formulario
  irAPaso(paso: number): void {
    if (paso < 0 || paso > this.pasosLabels.length - 1) return;
    
    // Validar el paso actual antes de avanzar
    if (paso > this.pasoActual) {
      if (!this.validarPasoActual()) return;
    }
    
    this.pasoActual = paso;
  }
  
  // Validar el paso actual
  validarPasoActual(): boolean {
    switch (this.pasoActual) {
      case 0: // Usuario
        return this.usuarioForm.valid;
      case 1: // Plan
        return this.planForm.valid;
      case 2: // Turnos
        const diasSeleccionadosControl = this.turnosForm.get('diasSeleccionados');
        const frecuenciaControl = this.planForm.get('frecuencia');
        
        const diasSeleccionados = diasSeleccionadosControl ? diasSeleccionadosControl.value : [];
        const frecuencia = frecuenciaControl ? frecuenciaControl.value : 3;
        
        if (diasSeleccionados.length !== frecuencia) {
          this.snackBar.open(`Debe seleccionar exactamente ${frecuencia} días`, 'Cerrar', {
            duration: 3000
          });
          return false;
        }
        return this.turnosForm.valid;
      case 3: // Pago
        return this.pagoForm.valid;
      default:
        return true;
    }
  }
  
  // Métodos para obtener datos para la plantilla
  getUsuarioDni(): string {
    const usuarioId = this.usuarioForm.get('usuarioId')?.value;
    const usuario = this.usuarios.find(u => u.id === usuarioId);
    return usuario ? usuario.dni : '';
  }
  
  getUsuarioSeleccionado(): any {
    const usuarioId = this.usuarioForm.get('usuarioId')?.value;
    return this.usuarios.find(u => u.id === usuarioId);
  }
  
  // Obtener nombre del plan seleccionado
  getPlanNombre(): string {
    const planIdControl = this.planForm.get('planId');
    const planId = planIdControl ? planIdControl.value : null;
    const plan = this.planes.find(p => p.id === planId);
    return plan ? plan.nombre : '';
  }
  
  // Obtener precio del plan seleccionado
  getPlanPrecio(): number {
    const planIdControl = this.planForm.get('planId');
    const planId = planIdControl ? planIdControl.value : null;
    const plan = this.planes.find(p => p.id === planId);
    return plan ? plan.precio : 0;
  }
  
  // Obtener nombre del día de la semana
  getDiaNombre(diaValor: string): string {
    const dia = this.diasSemana.find(d => d.valor === diaValor);
    return dia ? dia.nombre : '';
  }
  
  // Obtener descripción del método de pago
  getMetodoPagoNombre(): string {
    const metodoPagoControl = this.pagoForm.get('metodoPago');
    const metodoPago = metodoPagoControl ? metodoPagoControl.value : null;
    const metodo = this.metodosPago.find(m => m.valor === metodoPago);
    return metodo ? metodo.nombre : '';
  }
  
  // Guardar la inscripción
  guardarInscripcion(): void {
    if (!this.validarPasoActual()) return;
    
    this.isLoading = true;
    
    // Recopilar datos de todos los formularios
    const usuarioIdControl = this.usuarioForm.get('usuarioId');
    const planIdControl = this.planForm.get('planId');
    const frecuenciaControl = this.planForm.get('frecuencia');
    const fechaInicioControl = this.planForm.get('fechaInicio');
    const fechaFinControl = this.planForm.get('fechaFin');
    const diasSeleccionadosControl = this.turnosForm.get('diasSeleccionados');
    const registrarPagoControl = this.pagoForm.get('registrarPago');
    const montoControl = this.pagoForm.get('monto');
    const metodoPagoControl = this.pagoForm.get('metodoPago');
    const fechaPagoControl = this.pagoForm.get('fechaPago');
    const descripcionControl = this.pagoForm.get('descripcion');
    
    const inscripcionData = {
      usuario: {
        id: usuarioIdControl ? usuarioIdControl.value : null
      },
      plan: {
        id: planIdControl ? planIdControl.value : null
      },
      frecuencia: frecuenciaControl ? frecuenciaControl.value : 3,
      fechaInicio: fechaInicioControl ? fechaInicioControl.value : new Date(),
      fechaFin: fechaFinControl ? fechaFinControl.value : null,
      detalles: diasSeleccionadosControl ? diasSeleccionadosControl.value.map((dia: string) => {
        // Aquí deberíamos tener los turnos seleccionados para cada día
        // Por ahora usamos el primer turno disponible para cada día como ejemplo
        const turnosDelDia = this.getTurnosByDia(dia);
        return {
          diaSemana: dia,
          turnoId: turnosDelDia.length > 0 ? turnosDelDia[0].id : null
        };
      }) : [],
      pago: registrarPagoControl && registrarPagoControl.value ? {
        monto: montoControl ? montoControl.value : 0,
        metodoPago: metodoPagoControl ? metodoPagoControl.value : 'EFECTIVO',
        fechaPago: fechaPagoControl ? fechaPagoControl.value : new Date(),
        descripcion: descripcionControl ? descripcionControl.value : ''
      } : null
    };
    
    console.log('Datos de inscripción a guardar:', inscripcionData);
    
    // Simulación de envío al backend
    /*
    API endpoints:
    - POST /api/inscripciones (crear nueva)
    - PUT /api/inscripciones/{id} (actualizar existente)
    - POST /api/inscripciones/{id}/renovar (renovar inscripción)
    */
    
    setTimeout(() => {
      this.isLoading = false;
      
      let mensaje = '';
      if (this.isEditMode) {
        mensaje = 'Inscripción actualizada exitosamente';
      } else if (this.isRenovacionMode) {
        mensaje = 'Inscripción renovada exitosamente';
      } else {
        mensaje = 'Inscripción creada exitosamente';
      }
      
      this.snackBar.open(mensaje, 'Cerrar', {
        duration: 3000
      });
      
      // Redireccionar a la página del usuario
      const usuarioId = this.usuarioForm.get('usuarioId')?.value;
      if (usuarioId) {
        this.router.navigate(['/dashboard/usuarios', usuarioId]);
      } else {
        this.router.navigate(['/dashboard/usuarios']);
      }
    }, 2000);
  }
  
  cancelar(): void {
    // Si venimos de la página de un usuario, volver a ella
    if (this.usuarioId) {
      this.router.navigate(['/dashboard/usuarios', this.usuarioId]);
    } 
    // Si estamos editando una inscripción, volver a la página de inscripciones
    else if (this.inscripcionId) {
      this.router.navigate(['/dashboard/inscripciones']);
    }
    // De lo contrario, ir a la lista de usuarios
    else {
      this.router.navigate(['/dashboard/usuarios']);
    }
  }
}