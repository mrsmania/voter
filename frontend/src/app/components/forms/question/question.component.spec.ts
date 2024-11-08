import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionsComponent } from './question.component';

describe('QuestionsComponent', () => {
  let component: QuestionsComponent;
  let fixture: ComponentFixture<QuestionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuestionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
