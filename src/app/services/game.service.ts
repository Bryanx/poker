import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Room} from '../model/room';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  url = 'https://poker-game-service.herokuapp.com/api/rooms';
  // url = 'http://localhost:5000/api/rooms';

  constructor(private http: HttpClient) {
  }

  getRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(this.url);
  }
}
