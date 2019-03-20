import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserAccountComponent } from './user-account.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {TranslatePipe} from '../../translate.pipe';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {MatProgressBarModule, MatSnackBarModule} from '@angular/material';
import {Friend} from '../../model/friend';

describe('UserAccountComponent', () => {
  let component: UserAccountComponent;
  let fixture: ComponentFixture<UserAccountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserAccountComponent, TranslatePipe ],
      imports: [ HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule, AngularFontAwesomeModule, MatSnackBarModule, MatProgressBarModule ]
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
});
