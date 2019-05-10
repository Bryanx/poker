import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendsComponent } from './friends.component';
import {SearchComponent} from '../search/search.component';
import {TranslatePipe} from '../../translate.pipe';
import {NgModel, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {User} from '../../model/user';
import {By} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

describe('FriendsComponent', () => {
  let component: FriendsComponent;
  let fixture: ComponentFixture<FriendsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FriendsComponent, SearchComponent, TranslatePipe, NgModel ],
      imports: [ HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule, BrowserAnimationsModule, AngularFontAwesomeModule ]
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

  it('Should display friend', () => {
    const friend: User = new User();
    friend.id = '123456';
    friend.username = 'joske verstreaten';
    component.friends.push(friend);

    fixture.detectChanges();
    const h1Tag = fixture.debugElement.queryAll(By.css('.friend-row'));
    expect(h1Tag[0].nativeElement.innerHTML).toContain('joske verstreaten');

    component.friends.pop();
    fixture.detectChanges();
    const noFriendsTag = fixture.debugElement.query(By.css('h2'));
    expect(noFriendsTag.nativeElement.innerHTML).toContain('No_friends');
  });
});
