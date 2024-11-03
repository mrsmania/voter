import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PollService } from '../../services/poll.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-poll',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './poll.component.html'
})
export class PollComponent implements OnInit {
  poll: any;
  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private pollService: PollService,
    private router: Router
  ) {}

  ngOnInit() {
    const token = this.route.snapshot.paramMap.get('token');
    if (token) {
      this.loadPollData(token);
    } else {
      this.errorMessage = 'Invalid Poll Token';
    }
  }

  loadPollData(token: string) {
    this.pollService.getPollByToken(token).subscribe({
      next: (poll) => {
        this.poll = poll;
      },
      error: (error) => {
        this.errorMessage = error.error || 'Error loading poll';
      }
    });
  }
}
