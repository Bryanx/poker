import {ApplicationRef, Component, OnInit} from '@angular/core';
import {TranslateService} from './services/translate.service';
import {NotifierService} from 'angular-notifier';
import {Subscriber, Subscription} from 'rxjs';
import {Message} from '@stomp/stompjs';
import {User} from './model/user';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Notification} from './model/notification';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  notificationSub: Subscription;
  myself: User = User.create();

  constructor(private translate: TranslateService,
              private notifier: NotifierService,
              private webSocketService: RxStompService) {
  }

  ngOnInit(): void {
    this.initializeNotificationConnection();
    setTimeout(() => {
      this.showNotification('default', 'Good evening, you lovely person!');
      this.showNotification('default', 'you lovely person!');
      this.showNotification('default', 'Good evening');
    }, 500);
  }

  private initializeNotificationConnection() {
    console.log('initializing connection...');
    this.notificationSub = this.webSocketService.watch('/user/receive-notification').subscribe((message: Message) => {
      if (message) {
        console.log('JAAAA');
        const not: Notification = JSON.parse(message.body) as Notification;
        this.showNotification('default', not.message);
      }
    }, error => {
      console.log(error.error.error_description);
    });
    console.log(this.notificationSub);
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

  /**
   * Hide oldest notification
   */
  hideOldestNotification(): void {
    this.notifier.hideOldest();
  }

  /**
   * Hide newest notification
   */
  hideNewestNotification(): void {
    this.notifier.hideNewest();
  }

  /**
   * Hide all notifications at once
   */
  hideAllNotifications(): void {
    this.notifier.hideAll();
  }

  /**
   * Show a specific notification (with a custom notification ID)
   *
   * @param type    Notification type
   * @param message Notification message
   * @param id      Notification ID
   */
  showSpecificNotification(type: string, message: string, id: string): void {
    this.notifier.show({
      id,
      message,
      type
    });
  }

  /**
   * Hide a specific notification (by a given notification ID)
   *
   * @param id Notification ID
   */
  hideSpecificNotification(id: string): void {
    this.notifier.hide(id);
  }
}

