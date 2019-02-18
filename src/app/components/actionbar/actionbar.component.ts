import {Component, Input, OnInit} from '@angular/core';
import {Act} from '../../model/act';
import {ActType} from '../../model/actType';
import {Phase} from '../../model/phase';
import {RoundService} from '../../services/round.service';

@Component({
  selector: 'app-actionbar',
  templateUrl: './actionbar.component.html',
  styleUrls: ['./actionbar.component.scss']
})
export class ActionbarComponent implements OnInit {
  @Input() roundId: number;
  @Input() curPhase: Phase;

  sliderValue = 0;

  constructor(private roundservice: RoundService) {
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

  playAct(actType: string) {
    const act: Act = new Act();
    act.roundId = this.roundId;
    act.type = ActType[actType];
    act.phase = this.curPhase;
    act.playerId = 588;

    if (act.type === ActType['BET'] || act.type === ActType['RAISE']) {
      act.bet = this.sliderValue;
    } else {
      act.bet = 0;
    }

    //TODO: Uitwerken
    this.roundservice.addAct(act).subscribe();
  }
}
