import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PlayerComponent} from './player.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {PlayerCardComponent} from '../player-card/player-card.component';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';

describe('PlayerComponent', () => {
  let component: PlayerComponent;
  let fixture: ComponentFixture<PlayerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        PlayerComponent,
        PlayerCardComponent],
      imports: [HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions)]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlayerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
