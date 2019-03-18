import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ReplayComponent} from './replay.component';
import {TranslatePipe} from '../../translate.pipe';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('ReplayComponent', () => {
  let component: ReplayComponent;
  let fixture: ComponentFixture<ReplayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ReplayComponent, TranslatePipe],
      imports: [HttpClientTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
