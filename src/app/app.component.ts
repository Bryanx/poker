import {AfterViewChecked, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {TranslateService} from './services/other/translate.service';
import {Notification} from './model/notification';
import {AuthorizationService} from './services/security/authorization.service';
import {UserService} from './services/user.service';
import {User} from './model/user';
import {NotifierService} from 'angular-notifier';
import {HomeVisibleService} from './services/other/home-visible.service';
import {NotificationType} from './model/notificationType';
import {WebSocketService} from './services/web-socket.service';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {NotifyMessageService} from './services/notify-message.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('simpleFadeAnimation', [
      state('in', style({opacity: 1})),
      transition(':enter', [
        style({opacity: 0}),
        animate(500)
      ]),
      transition(':leave',
        animate(500, style({opacity: 0})))
    ])
  ]
})

export class AppComponent implements OnInit, OnDestroy, AfterViewChecked {
  homeButtonVisible: Boolean;
  onlyHome: Boolean;
  newNotification: Notification;
  myself: User = User.create();
  ws: any;

  // levels
  xpLabel: String = '';
  xpPrev: number;
  levelPrev: number;
  showXp: Boolean = false;

  constructor(private translate: TranslateService,
              private userService: UserService,
              private webSocketService: WebSocketService,
              private notifier: NotifierService,
              private homeObservable: HomeVisibleService,
              private auth: AuthorizationService,
              private cdRef: ChangeDetectorRef,
              private notifyMessageService: NotifyMessageService) {
  }

  ngOnInit(): void {
    this.notifyMessageService.getState().subscribe(newState => {
      if (newState) {
        this.getUnreadNotifications();
      }
    });
    this.homeObservable.getState().subscribe(newState => this.homeButtonVisible = newState);
    this.homeObservable.getHome().subscribe(newState => this.onlyHome = newState);
    this.checkIfAuthenticated();
  }

  ngOnDestroy(): void {
    if (this.ws !== undefined) {
      this.ws.disconnect();
    }
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
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
        this.ws.disconnect();
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
      this.xpPrev = user.xpTillNext;
      this.levelPrev = user.level;
      this.initializeConnections();
    });
  }

  /**
   * Shows _notifications to the screen if any are pushed by the web socket.
   */
  private initializeConnections() {
    this.ws = this.webSocketService.connectUserService();
    this.ws.connect({}, (frame) => {
      // Level socket
      this.ws.subscribe('/user/receive-myself/' + this.myself.id, (message) => {
        if (message) {
          const user: User = JSON.parse(message.body) as User;
          this.changeLevelParameters(user);
        }
      });

      // Notification socket
      this.ws.subscribe('/user/receive-notification/' + this.myself.id, (message) => {
        if (message) {
          const not: Notification = JSON.parse(message.body) as Notification;
          this.userService.readNotification(not.id).subscribe();
          this.showNotification(not);

          this.newNotification = not;
        }
      });
    });
  }

  private changeLevelParameters(user: User) {
    this.myself.chips = user.chips;
    this.myself.xpTillNext = user.xpTillNext;
    this.myself.thresholdTillNextLevel = user.thresholdTillNextLevel;
    this.myself.level = user.level;

    if (this.levelPrev !== user.level) {
      this.xpLabel = 'LEVEL UP!';
    } else {
      this.xpLabel = '+ ' + (user.xpTillNext - this.xpPrev) + 'xp';
    }
    this.levelPrev = user.level;

    if (user.xpTillNext - this.xpPrev !== 0) {

      this.xpPrev = user.xpTillNext;
      this.showXp = true;
      setTimeout(() => this.showXp = false, 3000);
    }
  }

  private showNotification(not: Notification) {
    let type;
    if (not.type === NotificationType.DELETE_PRIVATE_ROOM) {
      type = 'error';
    } else if (not.type === NotificationType.ADD_PRIVATE_ROOM) {
      type = 'success';
    } else {
      type = 'default';
    }

    this.notifier.notify(type, not.message);
  }

  /**
   * Shows all the unread _notifications of a specific user with al little welcome message.
   */
  private getUnreadNotifications() {
    this.userService.getUnReadNotifications().subscribe(nots => {
      this.notifier.notify('success', 'Welcome back bro! you received ' + nots.length + ' notification while you were away');
      nots.forEach(not => {
        this.showNotification(not);
        this.userService.readNotification(not.id).subscribe();
      });
    });
  }

  hasAuthentication(): boolean {
    return this.auth.isAuthenticated() && !this.auth.isAdmin();
  }

  isNormalUser(): boolean {
    return !this.auth.isAdmin();
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

