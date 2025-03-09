import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, CommonModule],
  template: `
    <div class="app-container">
      <div class="sidebar-container">
        <app-sidebar></app-sidebar>
      </div>
      <div class="content-container">
        <main class="main-content">
          <router-outlet></router-outlet>
        </main>
      </div>
      <div class="user-info-container">
        <div class="user-info">
          <img src="assets/images/avatar.jpg" alt="User avatar" class="user-avatar">
          <span class="user-name">{{username}}</span>
        </div>
        <button class="menu-button" (click)="toggleMenu()">
          <i class="menu-icon">⋮</i>
        </button>
        <div class="dropdown-menu" *ngIf="isMenuOpen">
          <ul>
            <li (click)="logout()">Cerrar sesión</li>
          </ul>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .app-container {
      display: flex;
      height: 100vh;
      position: relative;
    }
    
    .sidebar-container {
      width: 250px;
      background-color: #f8f9fa;
      border-right: 1px solid #e9ecef;
      z-index: 10;
    }
    
    .content-container {
      flex: 1;
      display: flex;
      flex-direction: column;
      background-color: #f5f5f5;
      overflow: hidden;
    }
    
    .main-content {
      flex: 1;
      padding: 20px;
      overflow: auto;
    }
    
    .user-info-container {
      position: fixed;
      bottom: 0;
      left: 0;
      width: 250px;
      background-color: #6C13F1;
      color: white;
      padding: 15px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      z-index: 20;
    }
    
    .user-info {
      display: flex;
      align-items: center;
    }
    
    .user-avatar {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      margin-right: 8px;
    }
    
    .user-name {
      font-size: 14px;
    }
    
    .menu-button {
      background: none;
      border: none;
      color: white;
      cursor: pointer;
      font-size: 20px;
      padding: 0;
    }
    
    .dropdown-menu {
      position: absolute;
      bottom: 60px;
      left: 0;
      width: 100%;
      background-color: white;
      border-top: 1px solid #e9ecef;
      border-right: 1px solid #e9ecef;
      box-shadow: 0 -4px 6px rgba(0, 0, 0, 0.1);
    }
    
    .dropdown-menu ul {
      list-style: none;
      padding: 0;
      margin: 0;
    }
    
    .dropdown-menu li {
      padding: 12px 16px;
      cursor: pointer;
      color: #333;
    }
    
    .dropdown-menu li:hover {
      background-color: #f5f5f5;
    }
  `]
})
export class MainLayoutComponent {
  username = '';
  isMenuOpen = false;
  
  constructor(private authService: AuthService, private router: Router) {
    this.username = localStorage.getItem('username') || 'Usuario';
  }
  
  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }
  
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}