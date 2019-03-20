import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserAccountComponent } from './user-account.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {TranslatePipe} from '../../translate.pipe';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {MatProgressBarModule, MatSnackBarModule} from '@angular/material';
import {Friend} from '../../model/friend';
import {User} from '../../model/user';
import {By} from '@angular/platform-browser';

describe('UserAccountComponent', () => {
  let component: UserAccountComponent;
  let fixture: ComponentFixture<UserAccountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserAccountComponent, TranslatePipe ],
      imports: [ HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule, AngularFontAwesomeModule, MatSnackBarModule,
        MatProgressBarModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('isFriends should work', () => {
    component.user.id = '123';
    const friend: Friend = new Friend();
    friend.userId = '123';
    component.myself.friends.push(friend);

    expect(component.isFriends()).toBeTruthy();
    component.myself.friends.pop();
    expect(component.isFriends()).toBeFalsy();
  });

  it('Should display all the information of a user', () => {
    const user: User = new User();
    user.username = 'jos vermeiren';
    user.wins = 5;
    user.gamesPlayed = 25;
    user.chips = 2566;
    component.user = user;
    fixture.detectChanges();

    const userNameTag = fixture.debugElement.query(By.css('h1'));
    const spanTags = fixture.debugElement.queryAll(By.css('.half-width span'));
    expect(userNameTag.nativeElement.innerHTML).toContain('jos vermeiren');
    expect(spanTags[0].nativeElement.innerHTML).toContain('5');
    expect(spanTags[1].nativeElement.innerHTML).toContain('2,566');
    expect(spanTags[2].nativeElement.innerHTML).toContain('25');
  });
});
