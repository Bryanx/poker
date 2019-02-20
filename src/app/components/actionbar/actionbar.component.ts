import {Component, Input, OnInit} from '@angular/core';
import {Act} from '../../model/act';
import {ActType} from '../../model/actType';
import {Phase} from '../../model/phase';
import {RoundService} from '../../services/round.service';
import {WebsocketService} from '../../services/websocket.service';

@Component({
  selector: 'app-actionbar',
  templateUrl: './actionbar.component.html',
  styleUrls: ['./actionbar.component.scss']
})
export class ActionbarComponent implements OnInit {
  @Input() roundId: number;
  @Input() curPhase: Phase;
  @Input() roomId: number;
  public actTypes: typeof ActType = ActType;

  sliderValue = 0;

  constructor(private roundservice: RoundService, private websocketService: WebsocketService) {

  }

  ngOnInit() {
    this.initializeGameConnection();
  }

  initializeGameConnection() {
    const server = this.websocketService.getServer();
    /*server.connect({}, () => {
      server.subscribe('/gameroom/receiveact/' + this.roomId, act => {
        act = JSON.parse(act.body);
        if (act) {
          console.log(act);
        }
      }, error => {
        console.log(error.error.error_description);
      });
    }, error => {
      console.log(error.error.error_description);
    });*/
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

    this.roundservice.sendAct(act, this.roomId);
    // this.roundservice.addAct(act).subscribe();

  }

  getPossibleActs() {
    /*this.roundservice.getPossibleActs(618).subscribe(result => {
    // TODO: uitwerken
      console.log(result);
    });*/
    return true;
  }
}
