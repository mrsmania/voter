import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-state',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './state.component.html'
})
export class StateComponent {
  state= '';

}
