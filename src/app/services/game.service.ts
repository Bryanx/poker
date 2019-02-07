import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Room} from '../model/room';
import {Act} from '../model/act';
import {Round} from '../model/round';

/**
 * This service is used to manage all the HTTP traffic of the
 * game micro-service.
 */
@Injectable({
  providedIn: 'root'
})
export class GameService {
  url = 'https://poker-game-service.herokuapp.com/api/rooms';
  // url = 'http://localhost:8081/api/rooms';

  constructor(private http: HttpClient) {
  }

  /**
   * Gives back all the rooms that are in the
   * database of the micro-service.
   */
  getRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(this.url);
  }

  getRoom(roomId: number): Observable<Room> {
    return this.http.get<Room>(this.url + '/' + roomId);
  }

  getCurrentRound(roomId: number): Observable<Round> {
    return this.http.get<Round>(this.url + '/' + roomId + '/rounds/current-round');
  }

  addAct(act: Act): Observable<Act> {
    return this.http.put<Act>(this.url, act);
  }
}
