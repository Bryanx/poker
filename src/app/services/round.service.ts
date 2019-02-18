import {Injectable} from '@angular/core';
import {Act} from '../model/act';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RoundService {
  // url = 'https://poker-game-service.herokuapp.com/api/rounds';
  url = 'http://localhost:5001/api/rounds';

  constructor(private http: HttpClient) {
  }

  /**
   * Saves an act that was played by the player to the backend.
   *
   * @param act The act that needs to be saved.
   */
  addAct(act: Act): Observable<Act> {
    console.log(act);
    return this.http.post<Act>(this.url + '/' + act.roundId, act);
  }
}
