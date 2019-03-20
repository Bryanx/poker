import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RoomCardComponent} from './room-card.component';
import {RouterTestingModule} from '@angular/router/testing';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

describe('RoomCardComponent', () => {
  let component: RoomCardComponent;
  let fixture: ComponentFixture<RoomCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RoomCardComponent],
      imports: [RouterTestingModule, BrowserAnimationsModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should give back the right router link', () => {
    component.isAdmin = true;
    expect(component.determineRouterLink()).toBe('/game-rooms/');

    component.isAdmin = false;
    component.inSettingMode = true;
    expect(component.determineRouterLink()).toBe('/rooms/private/edit/');

    component.inSettingMode = false;
    expect(component.determineRouterLink()).toBe('/rooms/');
  });
});
