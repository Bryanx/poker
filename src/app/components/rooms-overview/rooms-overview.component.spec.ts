import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RoomsOverviewComponent} from './rooms-overview.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';

describe('RoomsOverviewComponent', () => {
  let component: RoomsOverviewComponent;
  let fixture: ComponentFixture<RoomsOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RoomsOverviewComponent],
      imports: [RouterTestingModule, HttpClientTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
