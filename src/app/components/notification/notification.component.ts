import {Component, Input, OnInit} from '@angular/core';
import {NotifierService} from 'angular-notifier';
import {UserService} from '../../services/user.service';
import {Notification} from '../../model/notification';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {NotificationType} from '../../model/notificationType';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
  animations: [
    trigger('simpleFadeAnimation', [
      state('in', style({opacity: 1})),
      transition(':enter', [
        style({opacity: 0}),
        animate(75)
      ]),
      transition(':leave',
        animate(75, style({opacity: 0})))
    ])
  ]
})
export class NotificationComponent implements OnInit {
  _notifications: Notification[] = [];
  showPanel: boolean;

  constructor(private notifier: NotifierService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.showPanel = false;
    this.getAllNotifications();
  }

  @Input()
  set notifications(notification: Notification) {
    if (notification !== undefined) {
      console.log(notification);
      this._notifications.splice(0, 0, notification);
    }
  }

  private getAllNotifications() {
    this.userService.getNotifications().subscribe(nots => this._notifications = nots.reverse());
  }

  determineIcon(type: NotificationType) {
    if (type === NotificationType.FRIEND_REQUEST) {
      return '../../../assets/img/icons/friend_request_icon.svg';
    } else if (type === NotificationType.GLOBAL_MESSAGE) {
      return '../../../assets/img/icons/public_announcement.svg';
    } else {
      return '../../../assets/img/icons/game_request.svg';
    }
  }

  formatTime(dateTime: string) {
    const monthNames = [
      'January', 'February', 'March',
      'April', 'May', 'June', 'July',
      'August', 'September', 'October',
      'November', 'December'
    ];

    const data = dateTime.split('T');

    // get date
    const date = data[0].split('-');
    const day = date[2];
    const month = date[1].startsWith('0') ? date[1].charAt(1) : date[1];
    const monthConverted = monthNames[(month as unknown as number) - 1];
    const dateConstructed = day + ' ' + monthConverted;

    // time constructed
    const time = data[1].split(':');
    const hours = time[0];
    const minutes = time[1];
    const timeConstructed = hours + ':' + minutes;

    return dateConstructed + ' ' + timeConstructed;
  }

  handleDelete(id: number) {
    this._notifications = this._notifications.filter(not => not.id !== id);
    this.userService.deleteNotification(id).subscribe();
  }

  handleDeleteAll() {
    this._notifications = [];
    this.userService.deleteNotifications().subscribe();
  }

  detemineLink(not: Notification) {
    if (not.type === NotificationType.GAME_REQUEST) {
      return '/rooms/' + not.ref;
    } else {
      return '/user/' + not.ref;
    }
  }
}
