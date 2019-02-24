import {Component, Input} from '@angular/core';
import {TranslateService} from './services/translate.service';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Notification} from './model/notification';
import {AuthorizationService} from './services/authorization.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(private translate: TranslateService, private auth: AuthorizationService) {
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

