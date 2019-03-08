import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GlobalMessageComponent } from './global-message.component';
import {TranslatePipe} from '../../translate.pipe';
import {NgModel, ReactiveFormsModule} from '@angular/forms';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {MatSnackBarModule} from '@angular/material';

describe('GlobalMessageComponent', () => {
  let component: GlobalMessageComponent;
  let fixture: ComponentFixture<GlobalMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GlobalMessageComponent, TranslatePipe, NgModel ],
      imports: [ AngularFontAwesomeModule, HttpClientTestingModule, MatSnackBarModule, ReactiveFormsModule ]
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
