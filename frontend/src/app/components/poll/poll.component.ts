import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PollService} from '../../services/poll.service';
import {CommonModule} from '@angular/common';
import {ToastrService} from 'ngx-toastr';
import {FormsModule} from '@angular/forms';
import {Stomp} from '@stomp/stompjs';
import {BaseChartDirective} from 'ng2-charts';
import SockJS from 'sockjs-client';
import {ChartData, ChartOptions} from 'chart.js';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-poll',
  standalone: true,
  imports: [CommonModule, FormsModule, BaseChartDirective],
  templateUrl: './poll.component.html'
})
export class PollComponent implements OnInit, OnDestroy {
  poll: any;
  errorMessage: string | null = null;
  userEmail: string = '';
  private stompClient: any;

  public pieChartData: ChartData<'pie'>[] = [];
  public pieChartOptions: ChartOptions<'pie'> = {
    responsive: true,
    plugins: {
      legend: {
        position: 'left'
      }
    }
  };

  @ViewChildren(BaseChartDirective) charts!: QueryList<BaseChartDirective>;

  constructor(
    private route: ActivatedRoute,
    private pollService: PollService,
    private router: Router,
    private toastr: ToastrService
  ) {
  }

  ngOnInit() {
    this.connectWebSocket();
    const token = this.route.snapshot.paramMap.get('token');
    if (!token) {
      this.router.navigate(['/']);
      return;
    }
    this.pollService.getPollByToken(token).subscribe({
      next: (poll) => {
        this.poll = poll;
        this.initializeChartData();
      },
      error: (error) => {
        this.router.navigate(['/']);
        const errorMessage = error.error?.message || 'Failed to load poll';
        this.toastr.error(errorMessage);
      },
    });
  }

  initializeChartData() {
    const COLORS = ['#DB3A34', '#177E89', '#FFC857', '#ADEEE3', '#9966FF', '#0F162B', '#F4A261', '#2A9D8F', '#8A6D3B', '#E63946', '#FFB400', '#264653', '#6A0572', '#2B9348', '#F94144'];
    this.pieChartData = this.poll.questions.map((question: any) => ({
      labels: question.options.map((option: any) => option.text),
      datasets: [
        {
          data: question.options.map((option: any) => option.votes.length),
          backgroundColor: COLORS.slice(0, question.options.length),
          optionBorderColor: "#000000",
          borderWidth: 0
        }
      ]
    }));
  }

  connectWebSocket() {
    const socket = new SockJS(`${environment.backendUrl}/ws`);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, (frame: any) => {
      this.stompClient.subscribe('/topic/votes', (message: any) => {
        if (message.body) {
          this.refreshVotes();
        }
      });
    });
  }

  private refreshVotes() {
    this.pollService.getUpdatedVoteCounts(this.poll.id).subscribe({
      next: (updatedCounts) => {
        this.updateVoteCounts(updatedCounts);
        this.updateChartData();

        this.pollService.getVotesByEmailAndPollId(this.userEmail, this.poll.id).subscribe({
          next: (votes) => {
            this.highlightUserVotes(votes);
          }
        });
      },
      error: () => {
        this.errorMessage = 'Failed to load updated vote data';
      }
    });
  }

  private updateChartData() {
    // Update the chart data
    this.pieChartData.forEach((chartData: any, questionIndex: number) => {
      const question = this.poll.questions[questionIndex];
      question.options.forEach((option: any, optionIndex: number) => {
        const dataset = chartData.datasets[0];
        dataset.data[optionIndex] = option.votes.length;
      });
    });

    // Force update each chart
    this.charts.forEach((chart) => {
      chart.update(); // This triggers the Chart.js library to re-render
    });
  }

  private updateVoteCounts(updatedCounts: any) {
    updatedCounts.forEach((updatedCount: any) => {
      this.poll.questions.forEach((question: any) => {
        const option = question.options.find((o: any) => o.id === updatedCount.optionId);
        if (option) {
          option.votes = Array(updatedCount.votes); // Set votes as an array of length `votes`
        }
      });
    });
  }

  toggleVote(optionId: number) {

    this.pollService.vote(this.userEmail, optionId).subscribe({
      next: () => {
        this.toastr.success('Vote recorded!');
        this.pollService.getVotesByEmailAndPollId(this.userEmail, this.poll.id).subscribe({
          next: (votes) => {
            this.highlightUserVotes(votes);
          }
        });
      },
      error: (error) => {
        const errorMessage = error.error?.message || 'Failed to submit vote';
        this.toastr.error(errorMessage);
      }
    });
  }

  private highlightUserVotes(votes: any[]) {
    this.poll.questions.forEach((question: any) => {
      question.options.forEach((option: any) => {
        option.votedByUser = votes.some((vote: any) => vote.optionId === option.id && vote.userEmail === this.userEmail);
      });
    });
  }

  downloadCSV() {
    this.pollService.exportPollResults(this.poll.token).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'poll-results.csv';
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => {
        this.toastr.error('Failed to download CSV file.');
      },
    });
  }

  ngOnDestroy() {
    if (this.stompClient) {
      this.stompClient.disconnect(() => {
        console.log('Disconnected');
      });
    }
  }
}
