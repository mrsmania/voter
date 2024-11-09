import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PollService } from '../../services/poll.service';
import { CommonModule } from '@angular/common';
import {ToastrService} from 'ngx-toastr';

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
    private router: Router,
    private toastr: ToastrService,
  ) {}

  ngOnInit() {
    const token = this.route.snapshot.paramMap.get('token');
    if (!token) {
      this.router.navigate(['/']);
      return;
    }
    this.pollService.getPollByToken(token).subscribe({
      next: (poll) => {
        this.poll = poll;
      },
      error: (error) => {
        console.log(error);
        const errorMessage = error.error?.message || 'Failed to load poll';
        this.toastr.error(errorMessage);
      }
    });
  }

}
