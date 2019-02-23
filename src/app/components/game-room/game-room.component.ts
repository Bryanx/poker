import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {switchMap} from 'rxjs/operators';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {Player} from '../../model/player';
import {Notification} from '../../model/notification';
import {NotificationType} from '../../model/notificationType';
import {UserService} from '../../services/user.service';

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
              private userService: UserService,
              private gameService: GameService) {
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
   */
  getAllPlayers(): Object[] {
    return this.room.playersInRoom.concat(this.player);
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
