import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Act} from '../../model/act';
import {ActType} from '../../model/actType';
import {Phase} from '../../model/phase';
import {RoundService} from '../../services/round.service';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-actionbar',
  templateUrl: './actionbar.component.html',
  styleUrls: ['./actionbar.component.scss']
})
export class ActionbarComponent implements OnInit, OnDestroy {
  @Input() roundId: number;
  @Input() curPhase: Phase;
  @Input() roomId: number;
  public actTypes: typeof ActType = ActType;
  actSubscription: Subscription;

  sliderValue = 0;

  constructor(private roundService: RoundService, private websocketService: RxStompService) {
  }

  ngOnInit() {
    this.initializeGameConnection();
  }

  ngOnDestroy() {
    this.actSubscription.unsubscribe();
  }

  initializeGameConnection() {
    this.actSubscription = this.websocketService.watch('/gameroom/receiveact/' + this.roomId).subscribe((message: Message) => {
      if (message) {
        console.log(JSON.parse(message.body));
      }
    }, error => {
      console.log(error.error.error_description);
    });
  }

  formatLabel(value: number | null) {
    if (!value) {
      return 0;
    }

    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }

    return value;
  }

  playAct(actType: ActType) {
    const act: Act = new Act();
    act.roundId = 1;
    act.type = actType;
    act.phase = Phase.River;
    act.playerId = 588;

    if (act.type === 'BET' || act.type === 'RAISE') {
      act.bet = this.sliderValue;
    } else {
      act.bet = 0;
    }

    this.websocketService.publish({destination: '/gameroom/sendact/' + this.roomId, body: JSON.stringify(act)});
  }

  getPossibleActs() {
    /*this.roundservice.getPossibleActs(618).subscribe(result => {
    // TODO: uitwerken
      console.log(result);
    });*/
    return true;
  }
}
