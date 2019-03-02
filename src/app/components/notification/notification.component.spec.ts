import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NotificationComponent} from './notification.component';
import {RouterTestingModule} from '@angular/router/testing';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('NotificationComponent', () => {
  let component: NotificationComponent;
  let fixture: ComponentFixture<NotificationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NotificationComponent],
      imports: [RouterTestingModule, HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions)],
      providers: []
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
