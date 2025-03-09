import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { 
    path: '', 
    redirectTo: '/login', 
    pathMatch: 'full' 
  },
  { 
    path: 'login', 
    loadComponent: () => import('./features/auth/login/login.component').then(c => c.LoginComponent)
  },
  { 
    path: 'dashboard', 
    loadComponent: () => import('./layout/main-layout/main-layout.component').then(c => c.MainLayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/dashboard/dashboard/dashboard.component').then(c => c.DashboardComponent)
      },
      {
        path: 'usuarios',
        loadComponent: () => import('./features/usuarios/usuarios-list/usuarios-list.component').then(c => c.UsuariosListComponent)
      },
      {
        path: 'usuarios/:id',
        loadComponent: () => import('./features/usuarios/usuario-detail/usuario-detail.component').then(c => c.UsuarioDetailComponent)
      },
      {
        path: 'usuarios/editar/:id',
        loadComponent: () => import('./features/usuarios/usuario-form/usuario-form.component').then(c => c.UsuarioFormComponent)
      },
      {
        path: 'nuevo-usuario',
        loadComponent: () => import('./features/usuarios/usuario-form/usuario-form.component').then(c => c.UsuarioFormComponent)
      },
      {
        path: 'planes',
        loadComponent: () => import('./features/planes/planes-list/planes-list.component').then(c => c.PlanesListComponent)
      },
      {
        path: 'planes/nuevo',
        loadComponent: () => import('./features/planes/plan-form/plan-form.component').then(c => c.PlanFormComponent)
      },
      {
        path: 'planes/editar/:id',
        loadComponent: () => import('./features/planes/plan-form/plan-form.component').then(c => c.PlanFormComponent)
      },
      {
        path: 'turnos',
        loadComponent: () => import('./features/turnos/turnos-list/turnos-list.component').then(c => c.TurnosListComponent)
      },
      {
        path: 'turnos/nuevo',
        loadComponent: () => import('./features/turnos/turno-form/turno-form.component').then(c => c.TurnoFormComponent)
      },
      {
        path: 'turnos/editar/:id',
        loadComponent: () => import('./features/turnos/turno-form/turno-form.component').then(c => c.TurnoFormComponent)
      },
      {
        path: 'turnos/usuarios/:id',
        loadComponent: () => import('./features/turnos/turno-usuarios/turno-usuarios.component').then(c => c.TurnoUsuariosComponent)
      },
      {
        path: 'inscripciones/nueva',
        loadComponent: () => import('./features/inscripciones/inscripcion-form/inscripcion-form.component').then(c => c.InscripcionFormComponent)
      },
      {
        path: 'inscripciones/editar/:id',
        loadComponent: () => import('./features/inscripciones/inscripcion-form/inscripcion-form.component').then(c => c.InscripcionFormComponent)
      },
      {
        path: 'inscripciones/renovar/:id',
        loadComponent: () => import('./features/inscripciones/inscripcion-form/inscripcion-form.component').then(c => c.InscripcionFormComponent),
        data: { renovar: true }
      },
      {
        path: 'usuario/:usuarioId/inscripcion',
        loadComponent: () => import('./features/inscripciones/inscripcion-form/inscripcion-form.component').then(c => c.InscripcionFormComponent)
      }
      // Aquí se añadirán más rutas para cada funcionalidad
    ]
  },
  { 
    path: '**', 
    redirectTo: '/login' 
  }
];
