import {Component, HostListener, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {Subscription} from 'rxjs';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {Player} from '../../model/player';

import {Notification} from '../../model/notification';
import {NotificationType} from '../../model/notificationType';
import {AuthorizationService} from '../../services/authorization.service';
import {Round} from '../../model/round';
import {RoomService} from '../../services/room.service';
import {ChatComponent} from '../chat/chat.component';
import {Act} from '../../model/act';
import {PlayerComponent} from '../player/player.component';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-room',
  templateUrl: './game-room.component.html',
  styleUrls: ['./game-room.component.scss']
})
export class GameRoomComponent implements OnInit, OnDestroy {
  room: Room = Room.create();
  player: Player = Player.create();
  roomSubscription: Subscription;
  roundSubscription: Subscription;
  done: boolean;
  round: Round;
  joinRoomInterval: any;
  getRoundInterval: any;
  @ViewChild(ChatComponent) chatChild: ChatComponent;
  @ViewChildren(PlayerComponent) playerChildren: QueryList<PlayerComponent>;
  lastAct: Act;

  constructor(private curRouter: ActivatedRoute, private router: Router, private gameService: GameService,
              private webSocketService: RxStompService, private authorizationService: AuthorizationService,
              private roomService: RoomService, private userService: UserService) {

  }

  ngOnInit() {
    const roomId = this.curRouter.snapshot.paramMap.get('id') as unknown;

    this.getRoom(roomId as number);

    this.joinRoomInterval = setInterval(() => {
      if (this.room.gameRules !== undefined) {
        this.initializeRoomConnection();
        this.initializeRoundConnection();
        this.initializeWinnerConnection();
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
    if (this.roomSubscription !== undefined) {
      this.leaveRoom();
      this.roomSubscription.unsubscribe();
    }
    if (this.roundSubscription !== undefined) {
      this.roundSubscription.unsubscribe();
    }
  }

  /**
   * Subscribes to the room channel. All room changes will now be received here.
   */
  initializeRoomConnection() {
    this.roomSubscription = this.webSocketService.watch('/room/receive-room/' + this.room.id).subscribe((message: Message) => {
      if (message) {
        this.room = JSON.parse(message.body) as Room;
        // console.log(this.room);
      }
    }, error => {
      console.log(error.error.error_description);
    });
  }

  /**
   * Subscribes to the round channel. All round changes will now be received here.
   */
  initializeRoundConnection() {
    this.roundSubscription = this.webSocketService.watch('/room/receive-round/' + this.room.id).subscribe((message: Message) => {
      if (message) {
        this.round = JSON.parse(message.body) as Round;
        this.updatePlayersInRound();
        // console.log(this.round);
      }
    }, error => {
      console.log(error.error.error_description);
    });
  }

  /**
   <<<<<<< HEAD
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

  /**
   * Returns the players that are in the room including yourself.
   * Subscribes to the winner channel. Every time someone wins it is received here.
   */
  initializeWinnerConnection() {
    this.roundSubscription = this.webSocketService.watch('/room/receive-winner/' + this.room.id).subscribe((message: Message) => {
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
    }, error => {
      console.log(error.error.error_description);
    });
  }

  getRoom(id: number): void {
    this.gameService.getRoom(id).subscribe(room => {
      this.room = room as Room;

      if (this.room.playersInRoom.length >= this.room.gameRules.maxPlayerCount) {
        this.navigateToOverview();
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
    this.gameService.leaveRoom(this.room.id).subscribe();
  }

  /**
   * Joins the web-instance user to this room.
   */
  private joinRoom(): void {
    this.roomService.joinRoom(this.room.id).subscribe(player => {
      this.player = player;
    }, error => {
      console.log(error.error.message);
      this.navigateToOverview();
    });
  }

  /**
   * Navigates to the rooms overview.
   */
  private navigateToOverview(): void {
    this.router.navigateByUrl('/rooms').then(/* DO NOTHING WITH PROMISE */);
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
}
