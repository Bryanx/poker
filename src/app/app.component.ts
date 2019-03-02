import {Component, OnDestroy, OnInit} from '@angular/core';
import {TranslateService} from './services/translate.service';
import {Notification} from './model/notification';
import {AuthorizationService} from './services/authorization.service';
import {UserService} from './services/user.service';
import {Subscription} from 'rxjs';
import {User} from './model/user';
import {NotifierService} from 'angular-notifier';
import {WebSocketService} from './services/web-socket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  newNotification: Notification;
  notificationsSub: Subscription;
  myself: User;
  ws: any;

  constructor(private translate: TranslateService,
              private userService: UserService,
              private websocketService: WebSocketService,
              private notifier: NotifierService,
              private auth: AuthorizationService) {
  }

  ngOnInit(): void {
    this.checkIfAuthenticated();
  }

  ngOnDestroy(): void {
    if (this.ws !== undefined) {
      this.ws.disconnect();
    }
  }

  /**
   * Constantly checks if a client is authenticated.
   * If that is the case, the credentials will be loaded and a connection will be established with a
   * web socket connection.
   */
  private checkIfAuthenticated() {
    const intervalId = setInterval(() => {
      if (this.auth.isAuthenticated()) {
        clearInterval(intervalId);
        this.getCredentials();
        this.checkIfNotAuthenticated();
      }
    }, 750);
  }

  /**
   * Constantly checks if a client is not authenticated.
   * If that is the case, the subscription with the web socket will be terminated.
   */
  private checkIfNotAuthenticated() {
    const intervalId = setInterval(() => {
      if (!this.auth.isAuthenticated()) {
        clearInterval(intervalId);
        this.notificationsSub.unsubscribe();
        this.checkIfAuthenticated();
      }
    }, 750);
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
   * Shows _notifications to the screen if any are pushed by the web socket.
   */
  private initializeNotificationConnection() {
    this.ws = this.websocketService.connectUserService();
    this.ws.connect({}, (frame) => {
      this.ws.subscribe('/user/receive-notification/' + this.myself.id, (message) => {
        if (message) {
          const not: Notification = JSON.parse(message.body) as Notification;
          this.userService.readNotification(not.id).subscribe();
          this.notifier.notify('default', not.message);
          this.newNotification = not;
        }
      });
    });
  }

  hasAuthentication(): boolean {
    return this.auth.isAuthenticated();
  }

  /**
   * Sets the language for this website.
   *
   * @param lang The languae that will be used.
   */
  setLang(lang: string) {
    this.translate.use(lang);
  }
}

