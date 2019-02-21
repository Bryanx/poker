import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Act} from '../../model/act';
import {ActType} from '../../model/actType';
import {Phase} from '../../model/phase';
import {RoundService} from '../../services/round.service';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {Subscription} from 'rxjs';
import {Round} from '../../model/round';
import {AuthorizationService} from '../../services/authorization.service';
import {Card} from '../../model/card';
import {Player} from '../../model/player';

@Component({
  selector: 'app-actionbar',
  templateUrl: './actionbar.component.html',
  styleUrls: ['./actionbar.component.scss']
})
export class ActionbarComponent implements OnInit, OnDestroy {
  @Input() roomId: number;
  public actTypes: typeof ActType = ActType;
  actSubscription: Subscription;
  _round: Round;
  sliderValue = 0;
  myTurn: boolean;
  player: Player;

  constructor(private roundService: RoundService, private websocketService: RxStompService,
              private authorizationService: AuthorizationService) {
  }

  ngOnInit() {
    this.initializeGameConnection();
  }

  ngOnDestroy() {
    this.actSubscription.unsubscribe();
  }

  initializeGameConnection() {
    this.actSubscription = this.websocketService.watch('/room/receiveact/' + this.roomId).subscribe((message: Message) => {
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
    act.roundId = this._round.id;
    act.type = actType;
    act.phase = this._round.currentPhase;
    act.playerId = this.player.id;
    act.userId = this.player.userId;

    if (act.type === 'BET' || act.type === 'RAISE') {
      act.bet = this.sliderValue;
    } else {
      act.bet = 0;
    }

    this.websocketService.publish({destination: '/rooms/' + this.roomId + '/sendact', body: JSON.stringify(act)});
  }

  getPossibleActs() {
    /*this.roundservice.getPossibleActs(618).subscribe(result => {
    // TODO: uitwerken
      console.log(result);
    });*/
    return true;
  }

  @Input() set round(round: Round) {
    this._round = round;
    if (this._round) {
      this.getPlayer();
      this.checkTurn();
    }
  }

  getPlayer() {
    for (let i = 0; i < this._round.playersInRound.length; i++) {
      if (this._round.playersInRound[i].userId === this.authorizationService.getUserId()) {
        this.player = this._round.playersInRound[i];
      }
    }
  }

  checkTurn() {
    const nextPlayerIndex = this._round.button >= this._round.playersInRound.length - 1 ? 0 : this._round.button + 1;
    if (this._round.playersInRound[nextPlayerIndex].id === this.player.id) {
      console.log('Its my turn');
      console.log('First card:' + this.player.firstCard.type);
      console.log('Second card:' + this.player.secondCard.type);
      this.myTurn = true;
    } else {
      console.log('Its my opponents turn');
      console.log('First card:' + this.player.firstCard.type);
      console.log('Second card:' + this.player.secondCard.type);
      this.myTurn = false;
    }
  }
}
