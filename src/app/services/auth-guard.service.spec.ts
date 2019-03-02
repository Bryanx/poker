import { TestBed } from '@angular/core/testing';

import { AuthGuardService } from './auth-guard.service';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../notifierOptions';

describe('AuthGuardService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [ RouterTestingModule, HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions) ]
  }));

  it('should be created', () => {
    const service: AuthGuardService = TestBed.get(AuthGuardService);
    expect(service).toBeTruthy();
  });
});
