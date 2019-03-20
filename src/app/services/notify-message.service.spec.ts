import { TestBed } from '@angular/core/testing';

import { NotifyMessageService } from './notify-message.service';

describe('NotifyMessageService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NotifyMessageService = TestBed.get(NotifyMessageService);
    expect(service).toBeTruthy();
  });
});
