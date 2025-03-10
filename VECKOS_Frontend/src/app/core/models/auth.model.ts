export interface LoginRequest {
    username: string;
    password: string;
  }
  
  export interface LoginResponse {
    token: string;
    type: string;
    username: string;
    roles: string[];
  }