import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NotificationComponent} from './notification.component';
import {RouterTestingModule} from '@angular/router/testing';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {Notification} from '../../model/notification';
import {NotificationType} from '../../model/notificationType';
import {By} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

describe('NotificationComponent', () => {
  let component: NotificationComponent;
  let fixture: ComponentFixture<NotificationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NotificationComponent],
      imports: [RouterTestingModule, HttpClientTestingModule, BrowserAnimationsModule, NotifierModule.withConfig(customNotifierOptions)],
      providers: []
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('determine icon should work', () => {
    expect(component.determineIcon(NotificationType.FRIEND_REQUEST)).toBe('../../../assets/img/icons/friend_request_icon.svg');
    expect(component.determineIcon(NotificationType.GLOBAL_MESSAGE)).toBe('../../../assets/img/icons/public_announcement.svg');
    expect(component.determineIcon(NotificationType.ADD_PRIVATE_ROOM)).toBe('../../../assets/img/icons/game_request.svg');
    expect(component.determineIcon(NotificationType.DELETE_PRIVATE_ROOM)).toBe('../../../assets/img/icons/game_request.svg');
    expect(component.determineIcon(NotificationType.GAME_REQUEST)).toBe('../../../assets/img/icons/game_request.svg');
  });

  it('determineLink should work', () => {
    const not: Notification = new Notification();
    not.ref = '123';

    not.type = NotificationType.GAME_REQUEST;
    expect(component.detemineLink(not)).toBe('/rooms/123');

    not.type = NotificationType.FRIEND_REQUEST;
    expect(component.detemineLink(not)).toBe('/user/123');
  });

  it('should display notification', () => {
    const not: Notification = new Notification();
    not.ref = '123';
    not.timestamp = '2011-08-12T20:17:46.384Z';
    not.type = NotificationType.GAME_REQUEST;
    not.message = 'jos is awesome!';
    component.showPanel = true;

    component._notifications.push(not);
    fixture.detectChanges();
    const divMessage = fixture.debugElement.query(By.css('.message'));
    expect(divMessage.nativeElement.innerHTML).toContain('jos is awesome!');

    component._notifications.pop();
    fixture.detectChanges();
    const upToDateMessage = fixture.debugElement.query(By.css('.main-message'));
    expect(upToDateMessage.nativeElement.innerHTML).toContain('YOU ARE UP TO DATE BRO!');
  });
});
