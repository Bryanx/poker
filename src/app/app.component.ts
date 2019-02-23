import {Component, Input} from '@angular/core';
import {TranslateService} from './services/translate.service';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Notification} from './model/notification';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  showBell: boolean;

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

