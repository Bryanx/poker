import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import * as moment from 'moment';
import {Auth} from '../model/auth';
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
  private readonly tokenUrl: string;
  private readonly socialUrl: string;
  helper: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient,
              private urlService: UrlService,
              private userService: UserService,
              private notifier: NotifierService) {
    this.tokenUrl = urlService.authUrl;
    this.socialUrl = urlService.socialUrl;
  }


  login(loginPayload): Observable<Auth> {
    const headers = {
      'Authorization': 'Basic ' + btoa('my-trusted-client:secret'),
      'Content-type': 'application/x-www-form-urlencoded'
    };
    return this.http.post<Auth>(this.tokenUrl, loginPayload, {headers});
  }

  setSession(authResult: Auth) {
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
    if (localStorage.getItem('jwt_token')) {
      return this.helper.decodeToken(localStorage.getItem('jwt_token')).role === 'ROLE_ADMIN';
    }
    return false;
  }

  socialLogin(user: User) {
    return this.http.post<Auth>(this.socialUrl, user);
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

  getJwtToken() {
    if (localStorage.getItem('jwt_token')) {
      return localStorage.getItem('jwt_token');
    }
  }
}

