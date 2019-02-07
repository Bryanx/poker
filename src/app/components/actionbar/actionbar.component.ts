import {Component, Input, OnInit} from '@angular/core';
import {GameService} from '../../services/game.service';
import {Act} from '../../model/act';
import {ActType} from '../../model/actType';
import {Round} from '../../model/round';
import {Phase} from '../../model/phase';

@Component({
  selector: 'app-actionbar',
  templateUrl: './actionbar.component.html',
  styleUrls: ['./actionbar.component.scss']
})
export class ActionbarComponent implements OnInit {
  @Input() roundId: number;
  @Input() curPhase: Phase;

  sliderValue = 0;

  constructor(private gameService: GameService) {
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

  playAct(type: ActType) {
    const act: Act = new Act();
    act.roundId = this.roundId;
    act.type = type;
    act.phase = this.curPhase;
    act.playerId = 2;

    if (type === ActType.Bet || type === ActType.Raise) {
      act.bet = this.sliderValue;
    } else {
      act.bet = 0;
    }

    this.gameService.addAct(act);
  }
}
