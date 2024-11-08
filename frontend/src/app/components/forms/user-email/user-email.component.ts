import {Component, EventEmitter, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {PollService} from '../../../services/poll.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-user-email',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './user-email.component.html',
})
export class UserEmailComponent {
  @Output() usernameSubmitted = new EventEmitter<any>();
  userEmail: string = '';
  submittingUserEmail: boolean = false;

  constructor(private pollService: PollService, private toastr: ToastrService) {}

  submitUserEmail() {
    this.submittingUserEmail = true;
    this.pollService.submitUserEmail(this.userEmail).subscribe(
      (response) => {
        console.log('Successful response:', response);
        this.usernameSubmitted.emit(response);
        this.toastr.success('Poll created', 'Success');
      },
      (error) => {
        console.error('An Er:', error);
        this.toastr.error(error.error, 'Error');
        this.submittingUserEmail = false;
      },
      () => {
        console.log('Fetching complete');
      }
    );
  }

}
