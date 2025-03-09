export interface User {
    id?: number;
    nombre: string;
    apellido: string;
    fechaNacimiento: Date;
    dni: string;
    cuil?: string;
    telefono?: string;
    correo?: string;
    estado: 'ACTIVO' | 'INACTIVO' | 'PENDIENTE';
  }