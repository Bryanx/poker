import {AfterViewChecked, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Act} from '../../model/act';
import {ActType} from '../../model/actType';
import {RoundService} from '../../services/round.service';
import {Round} from '../../model/round';
import {AuthorizationService} from '../../services/authorization.service';
import {Player} from '../../model/player';
import {Room} from '../../model/room';
import {CurrentPhaseBet} from '../../model/currentPhaseBet';
import {WebSocketService} from '../../services/web-socket.service';

@Component({
  selector: 'app-actionbar',
  templateUrl: './actionbar.component.html',
  styleUrls: ['./actionbar.component.scss']
})
export class ActionbarComponent implements OnInit, OnDestroy {
  @Input() room: Room = Room.create();
  public actTypes: typeof ActType = ActType;
  _round: Round;
  sliderValue = 0;
  myTurn: boolean;
  player: Player;
  currentAct: Act;
  possibleActs: ActType[];
  currentPhaseBet: CurrentPhaseBet = CurrentPhaseBet.create();
  @Output() actEvent: EventEmitter<Act> = new EventEmitter<Act>();
  canAct = true;
  @Output() currentPhaseBetEvent: EventEmitter<CurrentPhaseBet> = new EventEmitter<CurrentPhaseBet>();
  ws: any;
  allIn: boolean;
  betraise: boolean;

  constructor(private roundService: RoundService, private websocketService: WebSocketService,
              private authorizationService: AuthorizationService) {
  }

  ngOnInit() {
    this.initializeGameConnection();
  }

  ngOnDestroy() {
    if (this.ws !== undefined) {
      this.ws.disconnect();
    }
  }

  /**
   * Subscribes to the act channel. All played acts will now be received here.
   */
  initializeGameConnection() {
    this.ws = this.websocketService.connectGameService();
    this.ws.connect({}, (frame) => {
      this.ws.subscribe('/room/receive-act/' + this.room.id, (message) => {
        if (message) {
          this.currentAct = JSON.parse(message.body) as Act;
          this.sliderValue = this.currentAct.totalBet;
          this.actEvent.emit(this.currentAct);
          const currentFaseBet = new CurrentPhaseBet();
          currentFaseBet.bet = this.currentAct.bet;
          currentFaseBet.seatNumber = this.currentAct.seatNumber;
          currentFaseBet.userId = this.currentAct.userId;
          this.currentPhaseBetEvent.emit(currentFaseBet);
        }
      });
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

  /**
   * Builds an act and sends it to the game service.
   */
  playAct(actType: ActType, allIn?: boolean) {
    // console.log(actType);
    if (this.canAct) {
      this.canAct = false;
      const act: Act = new Act();
      act.roundId = this._round.id;
      act.type = actType;
      act.phase = this._round.currentPhase;
      act.playerId = this.player.id;
      act.userId = this.player.userId;
      act.roomId = this.room.id;
      act.seatNumber = this.player.seatNumber;

      if (allIn) {
        act.allIn = true;
        act.bet = 0;
      } else {
        if (act.type === 'BET' || act.type === 'RAISE' || act.type === 'CALL') {
          act.bet = this.sliderValue - this.currentPhaseBet.bet;
          if (act.bet >= this.player.chipCount) {
            act.bet = this.player.chipCount;
            act.allIn = true;
          }
          this.currentPhaseBet.bet = this.currentPhaseBet.bet + this.sliderValue;
          this.currentPhaseBet.seatNumber = this.player.seatNumber;
        } else {
          act.bet = 0;
        }
        act.totalBet = this.sliderValue;
      }

      this.roundService.addAct(act).subscribe(() => {
      }, error => {
        console.log(error.error.message);
      });
      setTimeout(() => this.canAct = true, 3000);
    }
  }

  isActPossible(acttype: ActType) {
    if (this.possibleActs !== undefined) {
      return this.possibleActs.indexOf(acttype) > -1;
    } else {
      return false;
    }
  }

  @Input() set round(round: Round) {
    if (round !== undefined) {
      if (this._round === undefined) {
        this.currentPhaseBet.bet = 0;
      } else {
        if (round.currentPhase !== this._round.currentPhase) {
          this.currentPhaseBet.bet = 0;
        }
      }

      this._round = round;

      this.getPlayer();
      this.checkTurn();

      if (this.myTurn && this.player.allIn) {
        this.playAct(ActType.Check, this.player.allIn);
      }
    }
  }

  getPlayer() {
    this.player = this._round.playersInRound.find(player => player.userId === this.authorizationService.getUserId());
    if (this.player !== null) {
      this.currentPhaseBet.userId = this.player.userId;
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
      // console.log(this.possibleActs);
    });
  }

  getMimimumRaise() {
    if (this.currentAct !== undefined) {
      return this.currentAct.bet;
    } else {
      return 0;
    }
  }

  onChange(value: number) {
    if (value > 0) {
      if (value === this.player.chipCount) {
        this.allIn = true;
        this.betraise = false;
      } else {
        if (value > this.getMimimumRaise()) {
          this.allIn = false;
          this.betraise = true;
        } else {
          this.allIn = false;
          this.betraise = false;
        }
      }
    } else {
      this.allIn = false;
      this.betraise = false;
    }
  }
}
