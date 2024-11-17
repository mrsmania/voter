import { Component } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {HeaderComponent} from './components/header/header.component';
import {QrLinkComponent} from './components/qr-link/qr-link.component';
import {NgIf} from '@angular/common';



@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoginComponent, HeaderComponent, QrLinkComponent, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'frontend';
  isIndex = false;

  constructor(private router: Router) {
    this.router.events.subscribe(() => {
      this.isIndex = this.router.url === '/';
    });
  }
}
