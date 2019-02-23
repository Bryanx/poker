import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {switchMap} from 'rxjs/operators';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {Player} from '../../model/player';
import {RxStompService} from '@stomp/ng2-stompjs';

@Component({
  selector: 'app-room',
  templateUrl: './game-room.component.html',
  styleUrls: ['./game-room.component.scss']
})
export class GameRoomComponent implements OnInit, OnDestroy {
  room: Room = Room.create();
  player: Player = Player.create();

  constructor(private curRouter: ActivatedRoute,
              private router: Router,
              private gameService: GameService,
              private webSocketService: RxStompService) {
  }

  ngOnInit() {
    this.curRouter.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.gameService.getRoom(+params.get('id'));
    })).subscribe((room) => {
      this.room = room as Room;

      if (this.room.playersInRoom.length >= this.room.gameRules.maxPlayerCount) {
        this.navigateToOverview();
      }

      this.joinRoom();
    });
  }

  ngOnDestroy(): void {
    this.leaveRoom();
  }

  /**
   * Returns the players that are in the room including yourself.
   */
  getAllPlayers(): Object[] {
    return this.room.playersInRoom.concat(this.player);
  }

  sendGameRequest() {
    
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
    this.gameService.joinRoom(this.room.id)
      .subscribe(player => {
        this.player = player;
      });
  }

  /**
   * Navigates to the rooms overview.
   */
  private navigateToOverview(): void {
    this.router.navigateByUrl('/rooms').then(/* DO NOTHING WITH PROMISE */);
  }
}
