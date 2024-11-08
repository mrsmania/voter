import { Component } from '@angular/core';
import {UserEmailComponent} from '../forms/user-email/user-email.component';
import {QuestionComponent} from '../forms/question/question.component';
import {NgForOf, NgIf} from '@angular/common';
import {StateComponent} from '../forms/state/state.component';

@Component({
  selector: 'app-create-poll',
  templateUrl: './create-poll.component.html',
  imports: [
    UserEmailComponent,
    QuestionComponent,
    NgIf,
    NgForOf,
    StateComponent
  ],
  standalone: true,
})
export class CreatePollComponent {
  showQuestions = false;
  token: string = '';
  password: string = '';
  questions: any[] = [0];

  onUsernameSubmitted(response: any) {
    this.showQuestions = true;
    this.token = response.token;
    this.password = response.password;
  }

  addQuestion() {
    this.questions.push(this.questions.length);
  }

  removeQuestion(index: number) {
    if (this.questions.length > 1) {
      this.questions.splice(index, 1);
    }
    if (this.questions.length <= 1){

    }
  }

}
