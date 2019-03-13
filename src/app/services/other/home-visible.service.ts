import { Injectable } from '@angular/core';
import {Observable, Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HomeVisibleService {
  state: Subject<Boolean> = new Subject();

  getState(): Observable<Boolean> {
    return this.state;
  }

  emitNewState(state: boolean) {
    this.state.next(state);
  }
}
