import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

interface MenuItem {
  text: string;
  icon: string;
  route?: string;
  children?: MenuItem[];
  roles?: string[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  currentRoute = 'Dashboard';
  pagesOpen = true;
  profileOpen = false;
  usersOpen = true;

  constructor(private authService: AuthService) {}
  
  togglePages(): void {
    this.pagesOpen = !this.pagesOpen;
  }
  
  toggleProfile(): void {
    this.profileOpen = !this.profileOpen;
  }
  
  toggleUsers(): void {
    this.usersOpen = !this.usersOpen;
  }
}
