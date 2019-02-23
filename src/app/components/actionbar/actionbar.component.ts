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
import {el} from '@angular/platform-browser/testing/src/browser_util';
import {Room} from '../../model/room';

@Component({
  selector: 'app-actionbar',
  templateUrl: './actionbar.component.html',
  styleUrls: ['./actionbar.component.scss']
})
export class ActionbarComponent implements OnInit, OnDestroy {
  @Input() room: Room;
  public actTypes: typeof ActType = ActType;
  actSubscription: Subscription;
  _round: Round;
  sliderValue = 0;
  myTurn: boolean;
  player: Player;
  currentAct: Act;
  possibleActs: ActType[];

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
    this.actSubscription = this.websocketService.watch('/room/receive-act/' + this.room.id).subscribe((message: Message) => {
      if (message) {
        this.currentAct = JSON.parse(message.body) as Act;
        console.log(this.currentAct);
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
    console.log(actType);
    const act: Act = new Act();
    act.roundId = this._round.id;
    act.type = actType;
    act.phase = this._round.currentPhase;
    act.playerId = this.player.id;
    act.userId = this.player.userId;
    act.roomId = this.room.id;

    if (act.type === 'BET' || act.type === 'RAISE') {
      act.bet = this.sliderValue;
    } else {
      act.bet = 0;
    }

    this.roundService.addAct(act).subscribe(() => {}, error => {
      console.log(error.error.message);
    });
  }

  isActPossible(acttype: ActType) {
    if (this.possibleActs !== undefined) {
      if (this.possibleActs.indexOf(acttype) > -1) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
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
    this.myTurn = false;

    if (this.currentAct === undefined) {
      const nextPlayerIndex = this._round.button >= this._round.playersInRound.length - 1 ? 0 : this._round.button + 1;
      if (this._round.playersInRound[nextPlayerIndex].id === this.player.id) {
        this.myTurn = true;
      }
    } else {
      if (this.currentAct.nextUserId === undefined) {
        const nextPlayerIndex = this._round.button >= this._round.playersInRound.length - 1 ? 0 : this._round.button + 1;
        if (this._round.playersInRound[nextPlayerIndex].id === this.player.id) {
          this.myTurn = true;
        }
      } else {
        if (this.currentAct.nextUserId === this.player.userId) {
          this.myTurn = true;
        }
      }
    }

    this.roundService.getPossibleActs(this._round.id).subscribe(actTypes => {
      this.possibleActs = actTypes;
      console.log(this.possibleActs);
    });
  }
}
