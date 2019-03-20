import { TestBed } from '@angular/core/testing';

import { TranslateService } from './translate.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('TranslateService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule]
  }));

  it('should be created', () => {
    const service: TranslateService = TestBed.get(TranslateService);
    expect(service).toBeTruthy();
  });
});
