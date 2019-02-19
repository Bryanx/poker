import {Component, Input, OnInit} from '@angular/core';
import {Act} from '../../model/act';
import {ActType} from '../../model/actType';
import {Phase} from '../../model/phase';
import {RoundService} from '../../services/round.service';
import {AuthorizationService} from '../../services/authorization.service';

@Component({
  selector: 'app-actionbar',
  templateUrl: './actionbar.component.html',
  styleUrls: ['./actionbar.component.scss']
})
export class ActionbarComponent implements OnInit {
  @Input() roundId: number;
  @Input() curPhase: Phase;
  public actTypes: typeof ActType = ActType;

  sliderValue = 0;

  constructor(private roundservice: RoundService, private authoService: AuthorizationService) {
  }

  ngOnInit() {
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
    act.roundId = this.roundId;
    act.type = actType;
    act.phase = this.curPhase;
    act.playerId = 588;

    if (act.type === 'BET' || act.type === 'RAISE') {
      act.bet = this.sliderValue;
    } else {
      act.bet = 0;
    }

    this.roundservice.addAct(act).subscribe();
  }

  getPossibleActs() {
    /*this.roundservice.getPossibleActs(618).subscribe(result => {
    // TODO: uitwerken
      console.log(result);
    });*/
    return true;
  }
}
