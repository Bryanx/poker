import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {TranslatePipe} from '../../translate.pipe';
import {AuthService, AuthServiceConfig} from 'angularx-social-login';
import {provideConfig} from '../../app.module';
import {AuthorizationService} from '../../services/authorization.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoginComponent, TranslatePipe ],
      imports: [ FormsModule, ReactiveFormsModule, RouterTestingModule, HttpClientTestingModule ],
      providers: [ AuthorizationService, AuthService,
        {
          provide: AuthServiceConfig,
          useFactory: provideConfig
        } ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
