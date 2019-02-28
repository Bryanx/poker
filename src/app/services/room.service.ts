import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Round} from '../model/round';
import {HttpClient} from '@angular/common/http';
import {Player} from '../model/player';
import {Auth} from '../model/auth';
import {UrlService} from './url.service';
import {Room} from '../model/room';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  private readonly url: string;

  constructor(private http: HttpClient, private urlService: UrlService) {
    this.url = urlService.roomUrl;
  }

  joinRoom(roomId: number): Observable<Player> {
    return this.http.get<Player>(this.url + roomId + '/join');
  }

  leaveRoom(id: number): Observable<Player> {
    return this.http.delete<Player>(this.url + '/' + id + '/leave-room');
  }

  getCurrentRound(roomId: number): Observable<Round> {
    return this.http.get<Round>(this.url + roomId + '/current-round');
  }

  getRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(this.url);
  }

  getRoom(id: number): Observable<Room> {
    return this.http.get<Room>(this.url + '/' + id);
  }

  changeRoom(room: Room): Observable<Room> {
    return this.http.put<Room>(this.url + '/' + room.id, room);
  }

  addRoom(room: Room): Observable<Room> {
    return this.http.post<Room>(this.url, room);
  }

  deleteRoom(room: Room): Observable<Room> {
    return this.http.delete<Room>(this.url + '/' + room.id);
  }

  getPlayer(): Observable<Player> {
    return this.http.get<Player>(this.url + 'players');
  }

  changePlayer(player: Player): Observable<Player> {
    return this.http.put<Player>(this.url + '/players', player);
  }
}
