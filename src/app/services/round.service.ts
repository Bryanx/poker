import {Injectable} from '@angular/core';
import {Act} from '../model/act';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ActType} from '../model/actType';
import {UrlService} from './url.service';

@Injectable({
  providedIn: 'root'
})
export class RoundService {
  private readonly url: string;

  constructor(private http: HttpClient, private urlService: UrlService) {
    this.url = urlService.roundUrl;
  }

  /**
   * Saves an act that was played by the player to the backend.
   */
  getPossibleActs(roundId: number): Observable<ActType[]> {
    return this.http.get<ActType[]>(this.url + '/' + roundId + '/possible-acts');
  }


  /**
   * Ads an act to the current round.
   */
  addAct(act: Act): Observable<Act> {
    return this.http.post<Act>(this.url + '/act', act);
  }
}
