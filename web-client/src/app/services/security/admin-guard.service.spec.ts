import {TestBed} from '@angular/core/testing';

import {AdminGuardService} from './admin-guard.service';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';

describe('AdminGuardService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [RouterTestingModule, HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions)]
  }));

  it('should be created', () => {
    const service: AdminGuardService = TestBed.get(AdminGuardService);
    expect(service).toBeTruthy();
  });
});
