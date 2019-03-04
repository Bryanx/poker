import {Component, HostListener, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
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
import {PlayerComponent} from '../player/player.component';
import {CurrentPhaseBet} from '../../model/currentPhaseBet';
import {GameTableComponent} from '../game-table/game-table.component';
import {UserService} from '../../services/user.service';
import {Location} from '@angular/common';
import {WebSocketService} from '../../services/web-socket.service';
import {HomeVisibleService} from '../../services/home-visible.service';

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
  @ViewChild(GameTableComponent) gameTableChild: GameTableComponent;
  lastAct: Act;
  ws: any;

  constructor(private curRouter: ActivatedRoute, private router: Router, private websocketService: WebSocketService,
              private authorizationService: AuthorizationService, private roomService: RoomService, private userService: UserService,
              private location: Location, private homeObservable: HomeVisibleService) {}

  ngOnInit() {
    this.homeObservable.emitNewState(true);
    const roomId = this.curRouter.snapshot.paramMap.get('id') as unknown;

    this.getRoom(roomId as number);

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
          this.round = JSON.parse(message.body) as Round;
          this.updatePlayersInRound();
          // console.log(this.round);
        }
      });

      this.ws.subscribe('/room/receive-winner/' + this.room.id, (message) => {
        if (message) {
          const winningPlayer = JSON.parse(message.body) as Player;
          if (winningPlayer.userId === this.player.userId) {
            this.player = winningPlayer;
            this.chatChild.addMessage('You win, my bro');
            this.chatChild.addMessage('You had ' + this.player.handType);
          } else {
            this.roomService.getPlayer().subscribe((player: Player) => {
              this.player = player;
              this.chatChild.addMessage('You lose, my bro');
              this.chatChild.addMessage('You had ' + this.player.handType);
            });
          }
        }
      });
    });
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

  getRoom(id: number): void {
    this.roomService.getRoom(id).subscribe(room => {
      this.room = room as Room;

      if (this.room.playersInRoom.length >= this.room.gameRules.maxPlayerCount) {
        this.navigateBack();
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
    this.roomService.leaveRoom(this.room.id).subscribe();
  }

  /**
   * Joins the web-instance user to this room.
   */
  private joinRoom(): void {
    this.roomService.joinRoom(this.room.id).subscribe(player => {
      this.player = player;
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
    for (const player of this.room.playersInRoom ) {
      if (player.userId === currentPhaseBet.userId) {
        switch (currentPhaseBet.seatNumber) {
          case 0:
            this.gameTableChild.firstPlayer = currentPhaseBet;
            break;
          case 1:
            this.gameTableChild.secondPlayer = currentPhaseBet;
            break;
          case 2:
            this.gameTableChild.thirdPlayer = currentPhaseBet;
            break;
          case 3:
            this.gameTableChild.fourthPlayer = currentPhaseBet;
            break;
          case 4:
            this.gameTableChild.fifthPlayer = currentPhaseBet;
            break;
          case 5:
            this.gameTableChild.sixthPlayer = currentPhaseBet;
            break;
        }
      }
    }
  }
}
