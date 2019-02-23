import {ApplicationRef, Component, OnDestroy, OnInit} from '@angular/core';
import {TranslateService} from './services/translate.service';
import {NotifierService} from 'angular-notifier';
import {Subscriber, Subscription} from 'rxjs';
import {Message} from '@stomp/stompjs';
import {User} from './model/user';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Notification} from './model/notification';
import {AuthorizationService} from './services/authorization.service';
import {UserService} from './services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  notificationSub: Subscription;
  myself: User = User.create();

  constructor(private translate: TranslateService,
              private notifier: NotifierService,
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
  private initializeNotificationConnection() {;
    this.notificationSub = this.webSocketService.watch('/user/receive-notification/' + this.myself.id).subscribe((message: Message) => {
      if (message) {
        console.log('YES');
        const not: Notification = JSON.parse(message.body) as Notification;
        this.showNotification('default', not.message);
      }
    }, error => {
      console.log(error.error.error_description);
    });
  }

  /**
   * Sets the language for this website.
   *
   * @param lang The languae that will be used.
   */
  setLang(lang: string) {
    this.translate.use(lang);
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

