import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {User} from '../model/user';
import {catchError} from 'rxjs/operators';
import {AuthResult} from '../model/authResult';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  // url = 'https://poker-user-service.herokuapp.com/api/user';
  url = 'http://localhost:5000/api/user';

  constructor(private http: HttpClient) {
  }

  getUser(): Observable<User> {
    return this.http.get<User>(this.url);
  }

  addUser(user: User): Observable<AuthResult> {
    return this.http.post<AuthResult>(this.url, user);
  }

  changeUser(user: User): Observable<AuthResult> {
    return this.http.put<AuthResult>(this.url, user);
  }

  changePassword(user: any): Observable<AuthResult> {
    return this.http.patch<any>(this.url, user);
  }
}
