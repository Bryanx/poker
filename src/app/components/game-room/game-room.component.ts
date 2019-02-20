import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {switchMap} from 'rxjs/operators';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {Subscription} from 'rxjs';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {Player} from '../../model/player';
import {AuthorizationService} from '../../services/authorization.service';

@Component({
  selector: 'app-room',
  templateUrl: './game-room.component.html',
  styleUrls: ['./game-room.component.scss']
})
export class GameRoomComponent implements OnInit, OnDestroy {
  room: Room;
  player: Player;
  roomSubscription: Subscription;
  done: boolean;

  constructor(private curRouter: ActivatedRoute, private router: Router, private gameService: GameService,
              private websocketService: RxStompService, private authorizationService: AuthorizationService) {
  }

  ngOnInit() {
    const roomId = this.curRouter.snapshot.paramMap.get('id') as unknown;

    this.getRoom(roomId as number);

    setTimeout(() => this.initializeRoomConnection(), 500);
  }

  ngOnDestroy(): void {
    this.leaveRoom();
  }

  initializeRoomConnection() {
    this.roomSubscription = this.websocketService.watch('/gameroom/join/' + this.room.roomId).subscribe((message: Message) => {
      if (message) {
        console.log(JSON.parse(message.body));
        const player = JSON.parse(message.body) as Player;
        if (player.userId === this.authorizationService.getUserId()) {
          this.player = player;
          this.getRoom(this.room.roomId);
        } else {
          this.getRoom(this.room.roomId);
        }
      }
    }, error => {
      console.log(error.error.error_description);
    });

    setTimeout(() => {
      this.joinRoom();
      this.done = true;
    } , 500);
  }

  getRoom(id: number): void {
    this.gameService.getRoom(id).subscribe(room => {
      this.room = room as Room;

      if (this.room.playersInRoom.length >= this.room.gameRules.maxPlayerCount) {
        this.navigateToOverview();
      }
    });
  }

  /**
   * Returns the players that are in the room including yourself.
   */
  getAllPlayers(): Object[] {
    return this.room.playersInRoom;
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
    this.gameService.leaveRoom(this.room.roomId).subscribe();
  }

  /**
   * Joins the web-instance user to this room.
   */
  private joinRoom(): void {
    this.websocketService.publish({
      destination: '/rooms/' + this.room.roomId + '/join',
      body: JSON.stringify({userId: this.authorizationService.getUserId()})
    });
  }

  /**
   * Navigates to the rooms overview.
   */
  private navigateToOverview(): void {
    this.router.navigateByUrl('/rooms').then(/* DO NOTHING WITH PROMISE */);
  }
}
