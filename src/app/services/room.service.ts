import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Round} from '../model/round';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  // url = 'https://poker-game-service.herokuapp.com/api/rooms/';
  url = 'http://localhost:5001/api/rooms/';

  constructor(private http: HttpClient) {
  }

  getCurrentRound(roomId: number): Observable<Round> {
    return this.http.get<Round>(this.url + roomId + '/currentround');
  }
}
