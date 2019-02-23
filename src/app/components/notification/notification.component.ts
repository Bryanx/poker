import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {Subscription} from 'rxjs';
import {User} from '../../model/user';
import {NotifierService} from 'angular-notifier';
import {RxStompService} from '@stomp/ng2-stompjs';
import {UserService} from '../../services/user.service';
import {Message} from '@stomp/stompjs';
import {AuthorizationService} from '../../services/authorization.service';
import {Notification} from '../../model/notification';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
  animations: [
    trigger('simpleFadeAnimation', [
      state('in', style({opacity: 1})),
      transition(':enter', [
        style({opacity: 0}),
        animate(75 )
      ]),
      transition(':leave',
        animate(75, style({opacity: 0})))
    ])
  ]
})
export class NotificationComponent implements OnInit, OnDestroy {
  notifications: Notification[] = [];
  notificationSub: Subscription;
  myself: User = User.create();
  showBell: boolean;

  constructor(private notifier: NotifierService,
              private webSocketService: RxStompService,
              private userService: UserService,
              private authorizationService: AuthorizationService) {
  }

  ngOnInit(): void {
    this.showBell = false;
    this.checkIfAuthenticated();
    this.getAllNotifications();
  }

  ngOnDestroy(): void {
    this.notificationSub.unsubscribe();
  }

  handleClick() {
    this.showBell = !this.showBell;
  }

  private getAllNotifications() {
    this.userService.getNotifications().subscribe(nots => this.notifications = nots);
  }

  /**
   * Constantly checks if a client is authenticated.
   * If that is the case, the credentials will be loaded and a connection will be established with a
   * web socket connection.
   */
  private checkIfAuthenticated() {
    const intervalId = setInterval(() => {
      if (this.authorizationService.isAuthenticated()) {
        console.log('authenticated!');
        clearInterval(intervalId);
        this.getCredentials();
        this.checkIfNotAuthenticated();
      }
    }, 5000);
  }

  /**
   * Constantly checks if a client is not authenticated.
   * If that is the case, the subscription with the web socket will be terminated.
   */
  private checkIfNotAuthenticated() {
    const intervalId = setInterval(() => {
      if (!this.authorizationService.isAuthenticated()) {
        console.log('not authenticated!');
        clearInterval(intervalId);
        this.notificationSub.unsubscribe();
        this.checkIfAuthenticated();
      }
    }, 5000);
  }

  /**
   * Gets the user-credentials so that the connection with the web socket can be established.
   */
  private getCredentials() {
    this.userService.getMyself().subscribe(user => {
      this.myself = user;
      this.initializeNotificationConnection();
    });
  }

  /**
   * Shows notifications to the screen if any are pushed by the web socket.
   */
  private initializeNotificationConnection() {
    this.notificationSub = this.webSocketService.watch('/user/receive-notification/' + this.myself.id).subscribe((message: Message) => {
      if (message) {
        const not: Notification = JSON.parse(message.body) as Notification;
        this.userService.readNotification(not.id).subscribe();
        this.showNotification('default', not.message);
      }
    }, error => {
      console.log(error.error.error_description);
    });
  }

  /**
   * Show a notification
   *
   * @param type    Notification type
   * @param message Notification message
   */
  showNotification(type: string, message: string): void {
    this.notifier.notify(type, message);
  }
}
