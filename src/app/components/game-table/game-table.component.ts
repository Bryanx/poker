import {Component, Input, OnInit} from '@angular/core';
import {Round} from '../../model/round';
import {Phase} from '../../model/phase';
import {Player} from '../../model/player';
import {Room} from '../../model/room';

@Component({
  selector: 'app-game-table',
  templateUrl: './game-table.component.html',
  styleUrls: ['./game-table.component.scss']
})
export class GameTableComponent implements OnInit {
  _round: Round;
  firstCard: boolean;
  secondCard: boolean;
  thirdCard: boolean;
  fourthCard: boolean;
  fifthCard: boolean;
  @Input() room: Room = Room.create();

  constructor() { }

  ngOnInit() {
  }

  @Input() set round(round: Round) {
    this._round = round;

    if (this._round) {
      this.checkCards();
    }
  }

  checkCards() {
    switch (this._round.currentPhase) {
      case Phase.Flop:
        this.firstCard = true;
        this.secondCard = true;
        this.thirdCard = true;
        break;
      case Phase.Turn:
        this.firstCard = true;
        this.secondCard = true;
        this.thirdCard = true;
        this.fourthCard = true;
        break;
      case Phase.River:
        this.firstCard = true;
        this.secondCard = true;
        this.thirdCard = true;
        this.fourthCard = true;
        this.fifthCard = true;
        break;
      case Phase.Showdown:
        this.firstCard = true;
        this.secondCard = true;
        this.thirdCard = true;
        this.fourthCard = true;
        this.fifthCard = true;
        break;
      default:
        this.firstCard = false;
        this.secondCard = false;
        this.thirdCard = false;
        this.fourthCard = false;
        this.fifthCard = false;
    }
  }


}
