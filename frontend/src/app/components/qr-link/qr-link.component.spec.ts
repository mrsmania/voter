import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QrLinkComponent } from './qr-link.component';

describe('QrLinkComponent', () => {
  let component: QrLinkComponent;
  let fixture: ComponentFixture<QrLinkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QrLinkComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QrLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
