import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ActionbarComponent} from './actionbar.component';
import {CardComponent} from '../card/card.component';
import {MatSlider} from '@angular/material';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {TranslatePipe} from '../../translate.pipe';
import {RxStompService} from '@stomp/ng2-stompjs';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';


describe('ActionbarComponent', () => {
  let component: ActionbarComponent;
  let fixture: ComponentFixture<ActionbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ActionbarComponent, CardComponent, MatSlider, TranslatePipe],
      imports: [HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions)],
      providers: [RxStompService]
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
