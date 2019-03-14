import {Component, Input, OnInit} from '@angular/core';
import {Card} from '../../model/card';
import {Round} from '../../model/round';
import {Phase} from '../../model/phase';

@Component({
  selector: 'app-player-card',
  templateUrl: './player-card.component.html',
  styleUrls: ['./player-card.component.scss']
})
export class PlayerCardComponent implements OnInit {
  @Input() card: Card;
  @Input() round: Round = Round.create();

  constructor() { }

  ngOnInit() {
  }

  isShowDown() {
    return this.round.currentPhase === Phase.Showdown;
  }

  getSrc(): string {
    return '/assets/img/cards/' + this.card.type.toLowerCase() + '.svg';
  }
}
