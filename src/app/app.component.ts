import { Component } from '@angular/core';
import {TranslateService} from './services/translate.service';
import {AuthorizationService} from './services/authorization.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(private translate: TranslateService, private authorizationService: AuthorizationService, private router: Router) {
  }

  setLang(lang: string) {
    this.translate.use(lang);
  }

  logout() {
    this.authorizationService.logout();
    this.router.navigateByUrl('/');
  }

  isAuthenticated() {
    return this.authorizationService.isAuthenticated();
  }
}
