import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Room} from '../model/room';
import {Act} from "../model/act";

/**
 * This service is used to manage all the HTTP traffic of the
 * game micro-service.
 */
@Injectable({
  providedIn: 'root'
})
export class GameService {
  url = 'https://poker-game-service.herokuapp.com/api/rooms';
  // url = 'http://localhost:5000/api/rooms';

  constructor(private http: HttpClient) {
  }

  /**
   * Gives back all the rooms that are in the
   * database of the micro-service.
   */
  getRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(this.url);
  }

  addAct(act: Act): Observable<Act> {
    return this.http.put<Act>(this.url, act)
  }
}
