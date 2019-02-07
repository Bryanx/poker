import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  // url = 'https://poker-user-service.herokuapp.com/api/user';
  url = 'http://localhost:8081/api/user';

  constructor(private http: HttpClient) {
  }

  getUser(): Observable<User> {
    return this.http.get<User>(this.url);
  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.url, user);
  }
}
