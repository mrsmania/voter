import { Component } from '@angular/core';
import {UsernameComponent} from '../forms/username/username.component';

@Component({
  selector: 'app-create-poll',
  templateUrl: './create-poll.component.html',
  imports: [
    UsernameComponent
  ],
  standalone: true,
})
export class CreatePollComponent {
}
