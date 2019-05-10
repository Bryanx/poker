import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RegisterComponent} from './register.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TranslatePipe} from '../../translate.pipe';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RegisterComponent, TranslatePipe],
      imports: [FormsModule, ReactiveFormsModule, RouterTestingModule, HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions)]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
