import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {User} from '../model/user';
import {catchError} from 'rxjs/operators';
import {AuthResult} from '../model/authResult';
import {UrlService} from './url.service';

/**
 * This service is used to make API calls to the user micro service backend.
 */
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly url: string;

  constructor(private http: HttpClient, private urlService: UrlService) {
    this.url = urlService.userServiceUrl;
  }

  /**
   * Gives back your own credentials.
   */
  getMyself(): Observable<User> {
    return this.http.get<User>(this.url);
  }

  /**
   * Returns the requested user.
   *
   * @param userId The id of the requested user.
   */
  getUser(userId: string): Observable<User> {
    return this.http.get<User>(this.url + '/' + userId);
  }

  /**
   * Returns all the users that are in the system.
   */
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.url + 's');
  }

  /**
   * Gives back all the users that have the name-string inside
   * their name.
   *
   * @param name The name of the users.
   */
  getUsersByName(name: string): Observable<User[]> {
    return this.http.get<User[]>(this.url + 's/' + name);
  }

  /**
   * Adds a user to the system. Usually called on when a users registers.
   *
   * @param user The newly registered user.
   */
  addUser(user: User): Observable<AuthResult> {
    return this.http.post<AuthResult>(this.url, user);
  }

  /**
   * Changes one or more attributes of the user except the password.
   *
   * @param user The user that needs to be changed.
   */
  changeUser(user: User): Observable<AuthResult> {
    return this.http.put<AuthResult>(this.url, user);
  }

  /**
   * Changes the password of the given user.
   *
   * @param user The user with the new password.
   */
  changePassword(user: any): Observable<AuthResult> {
    return this.http.patch<any>(this.url, user);
  }
}
