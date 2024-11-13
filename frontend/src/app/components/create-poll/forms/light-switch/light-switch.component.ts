import {Component, Input} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-light-switch',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './light-switch.component.html'
})
export class LightSwitchComponent {
  @Input() active = false;
  @Input() title = 'Poll State'; // Default title
  @Input() inactiveText = 'Inactive'; // Default text for inactive state
  @Input() activeText = 'Active'; // Default text for active state

  getActiveData() {
    return this.active;
  }
}
