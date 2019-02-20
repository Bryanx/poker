import {Injectable} from '@angular/core';
import {Act} from '../model/act';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {WebsocketService} from './websocket.service';

@Injectable({
  providedIn: 'root'
})
export class RoundService {
  // url = 'https://poker-game-service.herokuapp.com/api/rounds';
  url = 'http://localhost:5001/api/rounds';
  channel = '/gameroom/sendact/';

  constructor(private http: HttpClient, private websocketService: WebsocketService) {
  }

  /**
   * Saves an act that was played by the player to the backend.
   */
  sendAct(act: Act, roomId: number): void {
    console.log(act);
    console.log(this.channel + roomId);
    this.websocketService.getServer().send(this.channel + roomId, {}, act);
  }

  /**
   * Saves an act that was played by the player to the backend.
   */
  getPossibleActs(roundId: number): Observable<Act> {
    return this.http.get<Act>(this.url + '/' + roundId + '/possible-acts');
  }
}
