
import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { PollService } from '../../services/poll.service';
import {Router, RouterLink} from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  @ViewChild('otpForm') otpForm!: ElementRef;

  constructor(private pollService: PollService, private router: Router, private toastr: ToastrService) {}

  ngAfterViewInit() {
    const form = this.otpForm.nativeElement as HTMLFormElement;
    const inputs = Array.from(form.querySelectorAll('input[type="text"]')) as HTMLInputElement[];
    const submit = form.querySelector('button[type="submit"]') as HTMLButtonElement;

    const handleKeyDown = (e: KeyboardEvent) => {
      if (!/^[a-zA-Z0-9_.-]$/.test(e.key) && e.key !== 'Backspace' && e.key !== 'Delete' && e.key !== 'Tab' && !e.metaKey) {
        e.preventDefault();
      }
      if (e.key === 'Delete' || e.key === 'Backspace') {
        const index = inputs.indexOf(e.target as HTMLInputElement);
        if (index > 0) {
          inputs[index - 1].value = '';
          inputs[index - 1].focus();
        }
      }
    };

    const handleInput = (e: Event) => {
      const target = e.target as HTMLInputElement;
      const index = inputs.indexOf(target);
      if (target.value && index < inputs.length - 1) {
        inputs[index + 1].focus();
      } else if (index === inputs.length - 1) {
        submit.focus();
      }
    };

    const handleFocus = (e: FocusEvent) => {
      (e.target as HTMLInputElement).select();
    };

    const handlePaste = (e: ClipboardEvent) => {
      e.preventDefault();
      const text = e.clipboardData?.getData('text') || '';
      const digits = text.split('');
      inputs.forEach((input, index) => (input.value = digits[index].toUpperCase() || ''));
      submit.focus();
    };

    submit.addEventListener('click', (e) => {
      e.preventDefault();
      const token = inputs.map((input) => input.value).join('');
      if (token.length !== 6) {
        const errorMessage = "Please enter a 6-digit token";
        this.toastr.error(errorMessage);
        return;
      }
      this.fetchPoll(token.toUpperCase());
    });

    inputs.forEach((input) => {
      input.addEventListener('input', handleInput);
      input.addEventListener('keydown', handleKeyDown);
      input.addEventListener('focus', handleFocus);
      input.addEventListener('paste', handlePaste);
    });
  }

  fetchPoll(token: string) {
    this.pollService.getPollByToken(token).subscribe({
      next: (poll) => {
        this.router.navigate(['/poll', token], { state: { poll } });
      },
      error: (error) => {
        const errorMessage = error.error?.message || 'Failed to save poll';
        this.toastr.error(errorMessage);
      }
    });
  }
}
