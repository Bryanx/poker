import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoomsOverviewComponent } from './rooms-overview.component';

describe('RoomsOverviewComponent', () => {
  let component: RoomsOverviewComponent;
  let fixture: ComponentFixture<RoomsOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoomsOverviewComponent ]
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
