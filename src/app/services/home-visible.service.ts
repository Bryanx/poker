import { Injectable } from '@angular/core';
import {Observable, Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HomeVisibleService {
  state: Subject<Boolean> = new Subject();
  onlyHome: Subject<Boolean> = new Subject();

  getState(): Observable<Boolean> {
    return this.state;
  }

  emitNewState(state: boolean) {
    this.state.next(state);
  }

  getHome(): Observable<Boolean> {
    return this.onlyHome;
  }

  emitHome(state: boolean) {
    this.onlyHome.next(state);
  }
}
