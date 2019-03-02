import { TestBed } from '@angular/core/testing';

import { AuthorizationService } from './authorization.service';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {NotifierModule, NotifierService} from 'angular-notifier';
import {NgModule} from '@angular/core';
import {AuthService, AuthServiceConfig} from 'angularx-social-login';
import {provideConfig} from '../app.module';
import {NotifierQueueService} from 'angular-notifier/src/services/notifier-queue.service';
import {customNotifierOptions} from '../notifierOptions';

describe('AuthorizationService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [ RouterTestingModule, HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions) ]
  }));

  it('should be created', () => {
    const service: AuthorizationService = TestBed.get(AuthorizationService);
    expect(service).toBeTruthy();
  });
});
