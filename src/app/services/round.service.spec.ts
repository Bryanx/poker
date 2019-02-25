import { TestBed } from '@angular/core/testing';

import { RoundService } from './round.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('RoundService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [ HttpClientTestingModule ]
  }));

  it('should be created', () => {
    const service: RoundService = TestBed.get(RoundService);
    expect(service).toBeTruthy();
  });
});
