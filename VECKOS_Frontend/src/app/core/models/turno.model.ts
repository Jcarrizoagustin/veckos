export interface Turno {
    id?: number;
    diaSemana: string; // 'MONDAY', 'TUESDAY', etc.
    hora: string;
    descripcion?: string;
  }