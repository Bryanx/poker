import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Room} from '../model/room';
import {Act} from '../model/act';
import {Player} from '../model/player';
import {UrlService} from './url.service';
import {PrivateRoom} from '../model/privateRoom';

/**
 * This service is used to manage all the HTTP traffic of the
 * game micro-service.
 */
@Injectable({
  providedIn: 'root'
})
export class GameService {
  private readonly url: string;

  constructor(private http: HttpClient, private urlService: UrlService) {
    this.url = urlService.gameServiceUrl;
  }

  /**
   * Gives back all the rooms that are in the
   * database of the micro-service.
   */
  getRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(this.url);
  }

  /**
   * Gives back all the private rooms that the user has the right authentication for.
   */
  getPrivateRooms(): Observable<PrivateRoom[]> {
    return this.http.get<PrivateRoom[]>(this.url + '/private');
  }

  /**
   * Gives back all the private rooms that the user owns.
   */
  getPrivateRoomsFromOwner(): Observable<PrivateRoom[]> {
    return this.http.get<PrivateRoom[]>(this.url + '/private/owner');
  }

  /**
   * Gives back a single room based on the id of the room.
   *
   * @param id The id of the room that needs to be returned.
   */
  getRoom(id: number): Observable<Room> {
    return this.http.get<Room>(this.url + '/' + id);
  }

  /**
   * Lets a user join into a room.
   *
   * @param id The id of the room that the player wants to join.
   */
  joinRoom(id: number): Observable<Player> {
    return this.http.post<Player>(this.url + '/' + id + '/join-room', '');
  }

  /**
   * Lets a player leave from the current room.
   *
   * @param id The id of the room that the player wants to leave.
   */
  leaveRoom(id: number): Observable<Player> {
    return this.http.delete<Player>(this.url + '/' + id + '/leave-room');
  }

  changeRoom(room: Room): Observable<Room> {
    return this.http.put<Room>(this.url + '/' + room.id, room);
  }

  addRoom(room: Room): Observable<Room> {
    return this.http.post<Room>(this.url, room);
  }

  addPrivateRoom(room: PrivateRoom): Observable<PrivateRoom> {
    return this.http.post<PrivateRoom>(this.url + '/private', room);
  }

  deleteRoom(room: Room): Observable<Room> {
    return this.http.delete<Room>(this.url + '/' + room.id);
  }

  getPlayer(): Observable<Player> {
    return this.http.get<Player>(this.url + '/players');
  }

  changePlayer(player: Player): Observable<Player> {
    return this.http.put<Player>(this.url + '/players', player);
  }


}
