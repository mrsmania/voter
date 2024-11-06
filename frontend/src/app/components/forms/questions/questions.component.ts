import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-questions',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './questions.component.html',
})
export class QuestionsComponent {
  inputs: string[] = ['', '']; // Initialize with two empty options

  addInputField() {
    this.inputs.push('');
  }

  removeInputField(index: number) {
    if (this.inputs.length > 2) { // Allow removal only if more than 2 options
      this.inputs.splice(index, 1);
    }
  }

  trackByIndex(index: number, item: any): number {
    return index;
  }
}
