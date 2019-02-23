import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {User} from '../../model/user';
import {NotifierService} from 'angular-notifier';
import {RxStompService} from '@stomp/ng2-stompjs';
import {UserService} from '../../services/user.service';
import {Message} from '@stomp/stompjs';
import {AuthorizationService} from '../../services/authorization.service';
import {Notification} from '../../model/notification';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit, OnDestroy {
  notificationSub: Subscription;
  myself: User = User.create();

  constructor(private notifier: NotifierService,
              private webSocketService: RxStompService,
              private userService: UserService,
              private authorizationService: AuthorizationService) {
  }

  ngOnInit(): void {
    this.checkIfAuthenticated();
  }

  ngOnDestroy(): void {
    this.notificationSub.unsubscribe();
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
