import { Component } from '@angular/core';
import {TranslateService} from './services/translate.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(public translate: TranslateService) {}

  setLang(lang: string) {
    this.translate.use(lang);
  }
}
