import {TestBed} from '@angular/core/testing';

import {RoomService} from './room.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('RoomService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
  });

  it('should be created', () => {
    const service: RoomService = TestBed.get(RoomService);
    expect(service).toBeTruthy();
  });
});
