import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import * as moment from 'moment';
import {AuthResult} from '../model/authResult';
import {Observable} from 'rxjs';
import {User} from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {

  constructor(private http: HttpClient) {}

  login(loginPayload): Observable<AuthResult> {
    const headers = {
      'Authorization': 'Basic ' + btoa('my-trusted-client:secret'),
      'Content-type': 'application/x-www-form-urlencoded'
    };
    // return this.http.post<AuthResult>('https://poker-user-service.herokuapp.com/oauth/token', loginPayload, {headers});
    return this.http.post<AuthResult>('http://localhost:5000/oauth/token', loginPayload, {headers});
  }

  setSession(authResult: AuthResult) {
    const expiresAt = moment().add(authResult.expires_in, 'second');

    localStorage.setItem('jwt_token', authResult.access_token);
    localStorage.setItem('expires_at', JSON.stringify(expiresAt.valueOf()));
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

  socialLogin(user: User) {
    return this.http.post<AuthResult>('http://localhost:5000/api/sociallogin', user);
  }
}
