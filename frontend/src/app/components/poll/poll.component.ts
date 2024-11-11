import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PollService } from '../../services/poll.service';
import { CommonModule } from '@angular/common';
import {ToastrService} from 'ngx-toastr';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-poll',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './poll.component.html'
})
export class PollComponent implements OnInit {
  poll: any;
  errorMessage: string | null = null;
  userEmail: string = '';


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
        this.router.navigate(['/']);
        const errorMessage = error.error?.message || 'Failed to load poll';
        this.toastr.error(errorMessage);
      }
    });
  }

  submitEmail() {
    this.pollService.getVotesByEmailAndPollId(this.userEmail, this.poll.id).subscribe({
      next: (votes) => {
        console.log(votes);
        this.highlightUserVotes(votes);
      },
      error: (error) => {
        const errorMessage = error.error?.message || 'Failed to load votes';
        this.toastr.error(errorMessage);
      }
    });
  }


  upsertVote(optionId: number) {
    this.pollService.vote(this.userEmail, optionId).subscribe({
      next: () => {
        this.toastr.success('Vote recorded!');
        this.toggleVoteCount(optionId); // Toggle the vote count in the UI
      },
      error: (error) => {
        const errorMessage = error.error?.message || 'Failed to submit vote';
        this.toastr.error(errorMessage);
      }
    });
  }


  private toggleVoteCount(optionId: number) {
    const option = this.poll.questions
      .flatMap((q: any) => q.options)
      .find((o: any) => o.id === optionId);

    if (option) {
      const existingVoteIndex = option.votes.findIndex((vote: any) => vote.userEmail === this.userEmail);
      if (existingVoteIndex > -1) {
        option.votes.splice(existingVoteIndex, 1);
      } else {
        option.votes.push({ userEmail: this.userEmail });
      }
    }
  }
  private highlightUserVotes(votes: any[]) {
    // Mark options as voted based on retrieved votes
    this.poll.questions.forEach((question: any) => {
      question.options.forEach((option: any) => {
        option.votedByUser = votes.some((vote: any) => vote.optionId === option.id);
      });
    });
  }

}
