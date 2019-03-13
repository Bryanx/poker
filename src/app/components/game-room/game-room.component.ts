import {Component, HostListener, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Room} from '../../model/room';
import {Player} from '../../model/player';

import {Notification} from '../../model/notification';
import {NotificationType} from '../../model/notificationType';
import {AuthorizationService} from '../../services/authorization.service';
import {Round} from '../../model/round';
import {RoomService} from '../../services/room.service';
import {ChatComponent} from '../chat/chat.component';
import {Act} from '../../model/act';
import {CurrentPhaseBet} from '../../model/currentPhaseBet';
import {UserService} from '../../services/user.service';
import {Location} from '@angular/common';
import {WebSocketService} from '../../services/web-socket.service';
import {HomeVisibleService} from '../../services/home-visible.service';
import {forkJoin} from 'rxjs';
import {User} from '../../model/user';
import {Phase} from '../../model/phase';

@Component({
  selector: 'app-room',
  templateUrl: './game-room.component.html',
  styleUrls: ['./game-room.component.scss']
})
export class GameRoomComponent implements OnInit, OnDestroy {
  room: Room = Room.create();
  player: Player = Player.create();
  done: boolean;
  round: Round;
  joinRoomInterval: any;
  getRoundInterval: any;
  @ViewChild(ChatComponent) chatChild: ChatComponent;
  lastAct: Act;
  ws: any;
  currentPhaseBets: CurrentPhaseBet[] = CurrentPhaseBet.createArrayOfSix();
  isJoined: boolean;

  constructor(private curRouter: ActivatedRoute, private router: Router, private websocketService: WebSocketService,
              private authorizationService: AuthorizationService, private roomService: RoomService, private userService: UserService,
              private location: Location, private homeObservable: HomeVisibleService) {
  }

  ngOnInit() {
    this.homeObservable.emitNewState(true);
    const roomId = this.curRouter.snapshot.paramMap.get('id') as unknown;
    this.getData(roomId as number);

    this.joinRoomInterval = setInterval(() => {
      if (this.room.gameRules.id !== 0) {
        this.initializeWebSocketConnection();
        this.joinRoom();
        clearInterval(this.joinRoomInterval);
      }
    }, 100);

    this.getRoundInterval = setInterval(() => {
      if (this.player.userId !== '0') {
        this.getCurrentRound();
        this.done = true;
        clearInterval(this.getRoundInterval);
      }
    }, 100);
  }

  ngOnDestroy(): void {
    this.homeObservable.emitNewState(false);
    if (this.ws !== undefined) {
      this.leaveRoom();
      this.ws.disconnect();
    }
  }

  /**
   * Subscribes to the room channel. All room changes will now be received here.
   * Subscribes to the round channel. All round changes will now be received here.
   * Subscribes to the winner channel. Every time someone wins it is received here.
   */
  initializeWebSocketConnection() {
    this.ws = this.websocketService.connectGameService();
    this.ws.connect({}, (frame) => {
      this.ws.subscribe('/room/receive-room/' + this.room.id, (message) => {
        if (message) {
          this.room = JSON.parse(message.body) as Room;
          // console.log(this.room);
        }
      });

      this.ws.subscribe('/room/receive-round/' + this.room.id, (message) => {
        if (message) {
          const round = JSON.parse(message.body) as Round;
          if (this.round !== undefined) {
            if (round.currentPhase !== this.round.currentPhase) {
              this.currentPhaseBets.forEach((x, index, theArray) => theArray[index].bet = 0);
            }
          }

          this.round = round;
          // console.log(this.round);
          this.updatePlayersInRound();
          const localPlayer = this.round.playersInRound.find(player => player.userId === this.authorizationService.getUserId());
          if (localPlayer !== null) {
            if (localPlayer.chipCount === 0 && !localPlayer.allIn) {
              this.leaveRoom();
              this.location.back();
            }
          }
        }
      });

      this.ws.subscribe('/room/receive-winner/' + this.room.id, (message) => {
        if (message) {
          const winningPlayer = JSON.parse(message.body) as Player;
          if (winningPlayer.userId === this.player.userId) {
            this.player = winningPlayer;
            this.userService.addXp(100).subscribe();
          } else {
            this.roomService.getPlayer().subscribe((player: Player) => {
              this.player = player;
            });
            this.userService.addXp(20).subscribe();
          }
          this.chatChild.addMessage('You had ' + this.player.handType);
        }
      });
    });
  }

  goBack() {
    return this.location.back();
  }

  /**
   * Sent a notification to someone for joining a game of poker.
   *
   * @param someoneId The person that needs to receive the request.
   */
  sendGameRequest(someoneId: string) {
    this.userService.getUser(someoneId).subscribe(user => {
      const notification = new Notification();
      notification.message = user.username + ' has sent you a request to join ' + this.room.name + ' room';
      notification.type = NotificationType.GAME_REQUEST;

      this.userService.sendNotification(user.id, notification).subscribe();
    });
  }

  getData(id: number): void {
    const getRoom = this.roomService.getRoom(id);
    const getMyself = this.userService.getMyself();

    forkJoin(getRoom, getMyself).subscribe(bundle => {
      this.room = bundle[0] as Room;
      if (this.room.playersInRoom.length >= this.room.gameRules.maxPlayerCount) {
        this.navigateBack();
      }

      const me = bundle[1] as User;
      if (me.level > this.room.gameRules.maxLevel || me.level < this.room.gameRules.minLevel) {
        this.location.back();
      }
    });
  }

  getCurrentRound(): void {
    this.roomService.getCurrentRound(this.room.id).subscribe(() => {
    }, (error => {
      console.log(error.error.message);
    }));
  }

  /**
   * This function is called when a page is refreshed.
   *
   * @param event The refresh event.
   */
  @HostListener('window:beforeunload', ['$event']) unloadHandler(event: Event) {
    this.leaveRoom();
  }

  /**
   * Calls the leave room API call in the game service.
   */
  private leaveRoom(): void {
    if (this.isJoined) {
      this.roomService.leaveRoom(this.room.id).subscribe();
    }
  }

  /**
   * Joins the web-instance user to this room.
   */
  private joinRoom(): void {
    this.roomService.joinRoom(this.room.id).subscribe(player => {
      this.player = player;
      this.isJoined = true;
    }, error => {
      console.log(error.error.message);
      this.navigateBack();
    });
  }

  /**
   * Navigates to the rooms overview.
   */
  private navigateBack(): void {
    this.location.back();
  }

  private updatePlayersInRound(): void {
    for (let i = 0; i < this.room.playersInRoom.length; i++) {
      for (const roundPlayer of this.round.playersInRound) {
        if (this.room.playersInRoom[i].userId === roundPlayer.userId) {
          this.room.playersInRoom[i] = roundPlayer;
        }
      }
    }
  }

  onActEvent(act: Act): void {
    this.lastAct = act;
  }

  onCurrentPhaseBetEvent(currentPhaseBet: CurrentPhaseBet) {
    this.currentPhaseBets.forEach((x, index, theArray) => {
      if (x.seatNumber === currentPhaseBet.seatNumber) {
        if (x.phase === Phase.Not_Started) {
          theArray[index] = currentPhaseBet;
        } else {
          if (x.phase === currentPhaseBet.phase) {
            theArray[index].bet = theArray[index].bet + currentPhaseBet.bet;
          } else {
            theArray[index].bet = currentPhaseBet.bet;
          }
        }
      }
    });
  }
}
