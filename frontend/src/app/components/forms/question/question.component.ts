import {Component, EventEmitter, Output, Input} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgForOf, NgIf} from '@angular/common';
import {PollService} from '../../../services/poll.service';

@Component({
  selector: 'app-question',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './question.component.html',
})
export class QuestionComponent {
  questionText: string = '';
  options: string[] = ['', '']; // Initialize with two empty options

  @Input() index!: number;  // Receive the question index
  @Output() delete = new EventEmitter<number>();  // Emit the delete action

  onDelete() {
    this.delete.emit(this.index);  // Emit the index to the parent component
  }

  addOptionField() {
    this.options.push('');
  }

  removeOptionField(index: number) {
    if (this.options.length > 2) { // Allow removal only if more than 2 options
      this.options.splice(index, 1);
    }
  }

  trackByIndex(index: number, item: any): number {
    return index;
  }
}
