import {Component, QueryList, ViewChildren} from '@angular/core';
import {UserEmailComponent} from './forms/user-email/user-email.component';
import {QuestionComponent} from './forms/question/question.component';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {StateComponent} from './forms/state/state.component';
import {PollService} from '../../services/poll.service';
import {QuestionService} from '../../services/question.service';
import {ToastrService} from 'ngx-toastr';
import {FormsModule} from '@angular/forms';
import {UpdatePollCredentialsComponent} from './forms/update-poll-credentials/update-poll-credentials.component';
import {LightSwitchComponent} from './forms/light-switch/light-switch.component';
import {Router} from '@angular/router';

@Component({
  selector: 'app-create-poll',
  templateUrl: './create-poll.component.html',
  imports: [
    UserEmailComponent,
    QuestionComponent,
    NgIf,
    NgForOf,
    StateComponent,
    FormsModule,
    UpdatePollCredentialsComponent,
    LightSwitchComponent,
    NgClass
  ],
  standalone: true,
})
export class CreatePollComponent {
  pollId: number | null = null;
  showQuestions = false;
  token: string = '';
  password: string = '';
  questions: any[] = []; // Initialize as empty array for new polls
  active = false;
  userEmail: string = '';
  submittingUserEmail: boolean = false;
  isDragging = false;


  showForms = true;

  @ViewChildren(QuestionComponent) questionComponents!: QueryList<QuestionComponent>;
  @ViewChildren(StateComponent) stateComponent!: QueryList<StateComponent>;

  constructor(private pollService: PollService, private questionService: QuestionService, private toastr: ToastrService, private router: Router) {
  }

  onUsernameSubmitted(response: any) {
    this.showQuestions = true;
    this.token = response.token;
    this.password = response.password;
    this.pollId = response.id;
    this.addQuestion();
    this.showForms = false;
  }


  loadPoll(credentials: { token: string; password: string; email: string }) {
    this.pollService.getPollByTokenPasswordEmail(credentials.token, credentials.password, credentials.email).subscribe({
      next: (poll) => {
        this.pollId = poll.id;
        this.token = poll.token;
        this.password = poll.password;
        if (poll.questions.length === 0) {
          this.addQuestion();
        } else {
          this.questions = poll.questions.map((q: any) => ({
            text: q.text,
            options: q.options.map((o: any) => o.text)
          }));
        }
        this.active = poll.active;
        this.showQuestions = true;
        this.toastr.success('Poll loaded successfully');
        this.showForms = false;
      },
      error: (error) => {
        const errorMessage = error.error?.message || 'Failed to load poll';
        this.toastr.error(errorMessage);
        console.error(error);
      }
    });
  }


  addQuestion() {
    this.questions.push({
      text: '',
      options: ['', '']
    });
  }

  removeQuestion(index: number) {
    if (this.questions.length > 1) {
      this.questions.splice(index, 1);
    }
  }

  createPoll() {
    this.submittingUserEmail = true;
    this.pollService.submitUserEmail(this.userEmail).subscribe({
      next: (response) => {
        this.onUsernameSubmitted(response);
        this.toastr.success('Poll created', 'Success');
      },
      error: (error) => {
        const errorMessage = error.error?.message || 'Failed to create poll';
        this.toastr.error(errorMessage);
      },
      complete: () => {
        this.submittingUserEmail = false;
      }
    });
  }

  savePoll() {
    const questionsData = this.questionComponents.map(question => question.getQuestionData());
    const activeState = this.stateComponent.first?.getActiveData();

    const pollData = {
      id: this.pollId,
      token: this.token,
      password: this.password,
      questions: questionsData,
      active: activeState,
    };

    this.pollService.savePoll(pollData).subscribe({
      next: () => {
        const extras = {state: {message: 'Poll saved successfully'}};
        if (activeState) {
          this.router.navigate([`/poll/${this.token}`], extras);
        } else {
          this.router.navigate(['/'], extras);
        }
      },
      error: (error) => {
        const errorMessage = error.error?.message || 'Failed to save poll';
        this.toastr.error(errorMessage);
        console.error(error);
      }
    });
  }

  // Handle file selection
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.uploadFile(input.files[0]);
    }
  }

  // Handle drag over event
  onDragOver(event: DragEvent): void {
    event.preventDefault(); // Prevent default behavior (open file in browser)
    this.isDragging = true;
  }

  // Handle drag leave event
  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.isDragging = false;
  }

  // Handle drop event
  onDrop(event: DragEvent): void {
    event.preventDefault(); // Prevent default behavior
    this.isDragging = false;

    if (event.dataTransfer && event.dataTransfer.files.length > 0) {
      const file = event.dataTransfer.files[0];
      this.uploadFile(file);
    }
  }

  // Upload file logic
  private uploadFile(file: File): void {
    if (file.type !== 'text/csv') {
      this.toastr.error('Only CSV files are allowed', 'Invalid File Type');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    this.questionService.uploadQuestions(formData).subscribe({
      next: (questions: any[]) => {
        this.toastr.success('Questions uploaded successfully');
        this.questions = this.questions.filter(q => q.text.trim() !== '' || q.options.some((o: string) => o.trim() !== ''));
        this.questions = [...this.questions, ...questions];
      },
      error: (error) => {
        const errorMessage = error.error?.message || 'Failed to upload questions';
        this.toastr.error(errorMessage);
        console.error(error);
      },
    });
  }
}

