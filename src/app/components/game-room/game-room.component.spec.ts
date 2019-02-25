import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GameRoomComponent} from './game-room.component';
import {PlayerComponent} from '../player/player.component';
import {ChatComponent} from '../chat/chat.component';
import {GameTableComponent} from '../game-table/game-table.component';
import {ActionbarComponent} from '../actionbar/actionbar.component';
import {FormsModule} from '@angular/forms';
import {CardComponent} from '../card/card.component';
import {MatSlider} from '@angular/material';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {TranslatePipe} from '../../translate.pipe';
import {RxStompService} from '@stomp/ng2-stompjs';

describe('GameRoomComponent', () => {
  let component: GameRoomComponent;
  let fixture: ComponentFixture<GameRoomComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        GameRoomComponent,
        PlayerComponent,
        ChatComponent,
        GameTableComponent,
        ActionbarComponent,
        CardComponent,
        MatSlider,
        TranslatePipe
      ],
      imports: [
        FormsModule,
        HttpClientTestingModule,
        RouterTestingModule,
      ],
      providers: [RxStompService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameRoomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
