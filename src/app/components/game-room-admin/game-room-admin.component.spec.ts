import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GameRoomAdminComponent } from './game-room-admin.component';

describe('GameRoomAdminComponent', () => {
  let component: GameRoomAdminComponent;
  let fixture: ComponentFixture<GameRoomAdminComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GameRoomAdminComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameRoomAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
