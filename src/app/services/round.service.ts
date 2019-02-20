import {Injectable} from '@angular/core';
import {Act} from '../model/act';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Round} from '../model/round';

@Injectable({
  providedIn: 'root'
})
export class RoundService {
  // url = 'https://poker-game-service.herokuapp.com/api/rounds/';
  url = 'http://localhost:5001/api/rounds/';

  constructor(private http: HttpClient) {
  }

  /**
   * Saves an act that was played by the player to the backend.
   */
  getPossibleActs(roundId: number): Observable<Act> {
    return this.http.get<Act>(this.url + roundId + '/possible-acts');
  }
}
