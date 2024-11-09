import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-poll-credentials',
  standalone: true,
  templateUrl: './update-poll-credentials.component.html',
  imports: [FormsModule]
})
export class UpdatePollCredentialsComponent {
  @Input() inputToken: string = '';
  @Input() inputPassword: string = '';
  @Input() inputEmail: string = '';

  @Output() loadPoll = new EventEmitter<{ token: string; password: string; email: string }>();

  onLoadPoll() {
    // Emit the values and log to verify structure
    const credentials = {
      token: this.inputToken,
      password: this.inputPassword,
      email: this.inputEmail
    };
    this.loadPoll.emit(credentials);
  }
}
