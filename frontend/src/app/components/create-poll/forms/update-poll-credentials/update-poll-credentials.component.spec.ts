import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePollCredentialsComponent } from './update-poll-credentials.component';

describe('UpdatePollCredentialsComponent', () => {
  let component: UpdatePollCredentialsComponent;
  let fixture: ComponentFixture<UpdatePollCredentialsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdatePollCredentialsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdatePollCredentialsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
