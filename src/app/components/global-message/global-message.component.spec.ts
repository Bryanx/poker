import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GlobalMessageComponent } from './global-message.component';

describe('GlobalMessageComponent', () => {
  let component: GlobalMessageComponent;
  let fixture: ComponentFixture<GlobalMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GlobalMessageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GlobalMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
