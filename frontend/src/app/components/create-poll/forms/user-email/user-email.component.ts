import {Component, EventEmitter, Input, Output} from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-email',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './user-email.component.html',
})
export class UserEmailComponent {
  @Input() userEmail: string = ''; // Accept the initial value
  @Input() submittingUserEmail: boolean = false;
  @Output() userEmailChange = new EventEmitter<string>(); // Emit changes for two-way binding
  @Output() emailSubmitted = new EventEmitter<string>();


  onEmailChange(value: string) {
    this.userEmail = value;
    this.userEmailChange.emit(this.userEmail); // Emit changes to parent
  }

  onSubmit() {
    this.emailSubmitted.emit(this.userEmail);
  }
}
