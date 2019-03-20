import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RoomsOverviewComponent} from './rooms-overview.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {TranslatePipe} from '../../translate.pipe';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';
import {RoomCardComponent} from '../room-card/room-card.component';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {WhiteListedUser} from '../../model/whiteListedUser';

describe('RoomsOverviewComponent', () => {
  let component: RoomsOverviewComponent;
  let fixture: ComponentFixture<RoomsOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RoomsOverviewComponent, TranslatePipe, RoomCardComponent],
      imports: [RouterTestingModule, HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions), AngularFontAwesomeModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('isInWhiteList should work', () => {
    const user1: WhiteListedUser = new WhiteListedUser();
    user1.userId = '123';
    const user2: WhiteListedUser = new WhiteListedUser();
    user2.userId = '456';
    const userArray = [user1, user2];

    expect(component.isInWhiteList('123', userArray)).toBeTruthy();
    expect(component.isInWhiteList('124', userArray)).toBeFalsy();
  });
});
