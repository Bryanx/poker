import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import * as moment from 'moment';
import {AuthResult} from '../model/authResult';
import {Observable} from 'rxjs';
import {User} from '../model/user';
import {JwtHelperService} from '@auth0/angular-jwt';
import {UrlService} from './url.service';
import {NotifierService} from 'angular-notifier';
import {UserService} from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {
  private readonly authUrl: string;
  private readonly socialUrl: string;
  helper: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient,
              private urlService: UrlService,
              private userService: UserService,
              private notifier: NotifierService) {
    this.authUrl = urlService.authUrl;
    this.socialUrl = urlService.socialUrl;
  }

  login(loginPayload): Observable<AuthResult> {
    const headers = {
      'Authorization': 'Basic ' + btoa('my-trusted-client:secret'),
      'Content-type': 'application/x-www-form-urlencoded'
    };
    return this.http.post<AuthResult>(this.authUrl, loginPayload, {headers});
  }

  setSession(authResult: AuthResult) {
    const expiresAt = moment().add(authResult.expires_in, 'second');

    localStorage.setItem('jwt_token', authResult.access_token);
    localStorage.setItem('expires_at', JSON.stringify(expiresAt.valueOf()));
    this.getUnreadNotifications();
  }

  logout() {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('expires_at');
  }

  isAuthenticated() {
    const expiration = localStorage.getItem('expires_at');

    if (expiration === null) {
      return false;
    }

    const expiresAt = JSON.parse(expiration);
    return moment().isBefore(moment(expiresAt));
  }

  isAdmin() {
    // TODO: For testing only admin1 = admin
      return this.getUsername().toLowerCase() === 'admin1';
  }

  socialLogin(user: User) {
    return this.http.post<AuthResult>(this.socialUrl, user);
  }

  getUsername() {
    let username = 'Party Parrot';
    if (localStorage.getItem('jwt_token')) {
      username = this.helper.decodeToken(localStorage.getItem('jwt_token')).username;
    }
    return username;
  }

  getUserId() {
    let userId = '1207';
    if (localStorage.getItem('jwt_token')) {
      userId = this.helper.decodeToken(localStorage.getItem('jwt_token')).uuid;
    }
    return userId;
  }

  /**
   * Shows all the unread _notifications of a specific user with al little welcome message.
   */
  private getUnreadNotifications() {
    this.userService.getUnReadNotifications().subscribe(nots => {
      this.notifier.notify('success', 'Welcome back bro! you received ' + nots.length + ' notification while you were away');
      nots.forEach(not => {
        this.notifier.notify('default', not.message);
        this.userService.readNotification(not.id).subscribe();
      });
    });
  }
}
