import { Component } from '@angular/core';
import {UsernameComponent} from '../forms/username/username.component';
import {QuestionsComponent} from '../forms/questions/questions.component';

@Component({
  selector: 'app-create-poll',
  templateUrl: './create-poll.component.html',
  imports: [
    UsernameComponent,
    QuestionsComponent
  ],
  standalone: true,
})
export class CreatePollComponent {
}
