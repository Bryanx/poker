import {Injectable} from '@angular/core';
import {Act} from '../model/act';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Round} from '../model/round';
import {ActType} from '../model/actType';

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
  getPossibleActs(roundId: number): Observable<ActType[]> {
    return this.http.get<ActType[]>(this.url + roundId + '/possible-acts');
  }
}
