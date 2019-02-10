import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ActionbarComponent} from './actionbar.component';
import {CardComponent} from '../card/card.component';
import {MatSlider} from '@angular/material';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('ActionbarComponent', () => {
  let component: ActionbarComponent;
  let fixture: ComponentFixture<ActionbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ActionbarComponent, CardComponent, MatSlider],
      imports: [HttpClientTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
