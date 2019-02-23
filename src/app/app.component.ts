import {ApplicationRef, Component, OnInit} from '@angular/core';
import {TranslateService} from './services/translate.service';
import {NotifierService} from 'angular-notifier';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  constructor(private translate: TranslateService, private notifier: NotifierService) {
  }

  ngOnInit(): void {
    setTimeout(() => {
      this.showNotification('default', 'Good evening, you lovely person!');
      this.showNotification('default', 'you lovely person!');
      this.showNotification('default', 'Good evening');
    }, 500);
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
  public showNotification(type: string, message: string): void {
    this.notifier.notify(type, message);
  }

  /**
   * Hide oldest notification
   */
  public hideOldestNotification(): void {
    this.notifier.hideOldest();
  }

  /**
   * Hide newest notification
   */
  public hideNewestNotification(): void {
    this.notifier.hideNewest();
  }

  /**
   * Hide all notifications at once
   */
  public hideAllNotifications(): void {
    this.notifier.hideAll();
  }

  /**
   * Show a specific notification (with a custom notification ID)
   *
   * @param type    Notification type
   * @param message Notification message
   * @param id      Notification ID
   */
  public showSpecificNotification(type: string, message: string, id: string): void {
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
  public hideSpecificNotification(id: string): void {
    this.notifier.hide(id);
  }
}

