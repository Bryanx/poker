import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendsComponent } from './friends.component';
import {SearchComponent} from '../search/search.component';
import {TranslatePipe} from '../../translate.pipe';
import {NgModel, ReactiveFormsModule} from '@angular/forms';
import {RouterLink, RouterModule} from '@angular/router';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {Friend} from '../../model/friend';
import {User} from '../../model/user';

describe('FriendsComponent', () => {
  let component: FriendsComponent;
  let fixture: ComponentFixture<FriendsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FriendsComponent, SearchComponent, TranslatePipe, NgModel ],
      imports: [ HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule, AngularFontAwesomeModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FriendsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should defriend someone', () => {
    const friend: User = new User();
    friend.id = '123456';

    component.friends.push(friend);
    expect(component.friends.length).toBe(1);
    component.removeFriend(friend.id);
    expect(component.friends.length).toBe(0);
  });
});
