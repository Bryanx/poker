import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GameRoomEditComponent } from './game-room-edit.component';
import {GameRoomComponent} from '../game-room/game-room.component';
import {PlayerComponent} from '../player/player.component';
import {ChatComponent} from '../chat/chat.component';
import {GameTableComponent} from '../game-table/game-table.component';
import {ActionbarComponent} from '../actionbar/actionbar.component';
import {CardComponent} from '../card/card.component';
import {MatSlider} from '@angular/material';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {TranslatePipe} from '../../translate.pipe';

describe('GameRoomEditComponent', () => {
  let component: GameRoomEditComponent;
  let fixture: ComponentFixture<GameRoomEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        GameRoomEditComponent,
        TranslatePipe
      ],
      imports: [
        FormsModule,
        HttpClientTestingModule,
        RouterTestingModule,
        ReactiveFormsModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameRoomEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
