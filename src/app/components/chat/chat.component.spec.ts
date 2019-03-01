import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ChatComponent} from './chat.component';
import {FormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';
import {TranslatePipe} from '../../translate.pipe';

describe('ChatComponent', () => {
  let component: ChatComponent;
  let fixture: ComponentFixture<ChatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ChatComponent, TranslatePipe],
      imports: [FormsModule, HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions)],
      providers: []
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
