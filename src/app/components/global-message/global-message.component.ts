import {Component, OnInit} from '@angular/core';
import {Notification} from '../../model/notification';
import {NotificationType} from '../../model/notificationType';
import {UserService} from '../../services/user.service';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-global-message',
  templateUrl: './global-message.component.html',
  styleUrls: ['./global-message.component.scss'],
  animations: [
    trigger('simpleFadeAnimation', [
      state('in', style({opacity: 1})),
      transition(':enter', [
        style({opacity: 0}),
        animate(200)
      ]),
    ])
  ]
})
export class GlobalMessageComponent implements OnInit {
  notifications: Notification[] = [];
  inputString: String = '';

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getNotifications().subscribe(nots => this.notifications = nots.reverse());
  }

  sendMessage() {
    if (this.inputString !== '') {
      const not: Notification = new Notification();
      not.message = this.inputString as string;
      not.type = NotificationType.GLOBAL_MESSAGE;
      not.read = false;
      this.userService.sendPublicNotification(not).subscribe(newNot => this.notifications.push(newNot));
    }
  }

  removeNotification(id: number) {
    this.notifications = this.notifications.filter(not => not.id !== id);
    this.userService.deleteNotification(id).subscribe();
  }
}
