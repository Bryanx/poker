import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Round} from '../model/round';
import {HttpClient} from '@angular/common/http';
import {Player} from '../model/player';
import {Auth} from '../model/auth';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  // url = 'https://poker-game-service.herokuapp.com/api/rooms/';
  url = 'http://localhost:5001/api/rooms/';

  constructor(private http: HttpClient) {
  }

  getCurrentRound(roomId: number): Observable<Round> {
    return this.http.get<Round>(this.url + roomId + '/current-round');
  }

  joinRoom(roomId: number): Observable<Player> {
    return this.http.get<Player>(this.url + roomId + '/join');
  }

  getPlayer(): Observable<Player> {
    return this.http.get<Player>(this.url + 'players');
  }
}
