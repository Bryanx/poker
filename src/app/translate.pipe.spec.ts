import { TranslatePipe } from './translate.pipe';
import {TestBed} from '@angular/core/testing';
import {TranslateService} from './services/translate.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('TranslatePipe', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule]
  }));

  it('create an instance', () => {
    const translateService: TranslateService = TestBed.get(TranslateService);
    const pipe = new TranslatePipe(translateService);
    expect(pipe).toBeTruthy();
  });
});
