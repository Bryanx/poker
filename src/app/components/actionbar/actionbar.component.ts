import {Component, OnInit} from '@angular/core';
import {GameService} from "../../services/game.service";
import {Act} from "../../model/act";
import {ActType} from "../../model/actType";

@Component({
  selector: 'app-actionbar',
  templateUrl: './actionbar.component.html',
  styleUrls: ['./actionbar.component.scss']
})
export class ActionbarComponent implements OnInit {
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
    act: Act = new Act(
      roundId= number,
      playerId= number,
      type = type,
      phase = string,
      bet = number
    )
  }
}
