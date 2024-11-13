import {Component, EventEmitter, Output, Input, ViewChildren, QueryList} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgForOf, NgIf} from '@angular/common';
import {PollService} from '../../../../services/poll.service';
import {LightSwitchComponent} from '../light-switch/light-switch.component';

@Component({
  selector: 'app-question',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    NgForOf,
    NgIf,
    LightSwitchComponent
  ],
  templateUrl: './question.component.html',
})
export class QuestionComponent {

  multipleChoice = false;
  @Input() questionText: string = '';
  @Input() options: string[] = ['', ''];
  @Input() index!: number;
  @Output() delete = new EventEmitter<number>();

  @ViewChildren(LightSwitchComponent) multipleChoiceComponent!: QueryList<LightSwitchComponent>;

  onDelete() {
    this.delete.emit(this.index);
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

  getQuestionData() {
    const multipleChoiceState = this.multipleChoiceComponent.first?.getActiveData();
    return {
      text: this.questionText,
      options: this.options.map(optionText => ({ text: optionText })),
      multipleChoice: multipleChoiceState
    };
  }
}
