import { TestBed } from '@angular/core/testing';

import { UrlService } from './url.service';

describe('UrlService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UrlService = TestBed.get(UrlService);
    expect(service).toBeTruthy();
  });
});
