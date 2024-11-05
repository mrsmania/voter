import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-username',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './username.component.html',
})
export class UsernameComponent {
  username: string = '';
  submitUsername() {
    console.log(this.username);
  }
}
