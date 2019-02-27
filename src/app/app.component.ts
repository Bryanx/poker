import {Component, OnInit} from '@angular/core';
import {TranslateService} from './services/translate.service';
import {Notification} from './model/notification';
import {AuthorizationService} from './services/authorization.service';
import {UserService} from './services/user.service';
import {Subscription} from 'rxjs';
import {User} from './model/user';
import {Message} from '@stomp/stompjs';
import {RxStompService} from '@stomp/ng2-stompjs';
import {NotifierService} from 'angular-notifier';
import {HomeVisibleService} from './services/home-visible.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  homeVisible: Boolean;
  newNotification: Notification;
  notificationsSub: Subscription;
  myself: User;

  constructor(private translate: TranslateService,
              private userService: UserService,
              private webSocketService: RxStompService,
              private notifier: NotifierService,
              private homeObservable: HomeVisibleService,
              private auth: AuthorizationService) {
  }

  ngOnInit(): void {
    this.homeObservable.getState().subscribe(state => this.homeVisible = state);
    this.checkIfAuthenticated();
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
    this.notificationsSub = this.webSocketService.watch('/user/receive-notification/' + this.myself.id).subscribe((message: Message) => {
      if (message) {
        const not: Notification = JSON.parse(message.body) as Notification;
        this.userService.readNotification(not.id).subscribe();
        this.notifier.notify('default', not.message);
        this.newNotification = not;
      }
    }, error => {
      console.log(error.error.error_description);
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

