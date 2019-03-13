import { TestBed } from '@angular/core/testing';

import { HomeVisibleService } from './home-visible.service';

describe('HomeVisibleService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HomeVisibleService = TestBed.get(HomeVisibleService);
    expect(service).toBeTruthy();
  });
});
