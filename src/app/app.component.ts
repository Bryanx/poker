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
export class AppComponent{
  constructor(private translate: TranslateService) {
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

