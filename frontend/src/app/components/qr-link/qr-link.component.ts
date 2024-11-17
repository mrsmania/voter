import { Component } from '@angular/core';
import {QRCodeModule} from 'angularx-qrcode';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-qr-link',
  standalone: true,
  imports: [QRCodeModule],
  templateUrl: './qr-link.component.html'
})
export class QrLinkComponent {
  public myAngularxQrCode: string = '';
  constructor() {
    this.myAngularxQrCode = `${environment.url}`;
  }
}
