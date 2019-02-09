import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {switchMap} from 'rxjs/operators';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {Player} from '../../model/player';

@Component({
  selector: 'app-room',
  templateUrl: './game-room.component.html',
  styleUrls: ['./game-room.component.scss']
})
export class GameRoomComponent implements OnInit, OnDestroy {
  room: Room = {
    roomId: 0,
    name: '',
    gameRules: null,
    playersInRoom: []
  };
  player: Player = null;

  constructor(private router: ActivatedRoute, private gameService: GameService) {
  }

  ngOnInit() {
    this.router.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.gameService.getRoom(+params.get('id'));
    })).subscribe((room) => {
      this.room = room as Room;
      this.joinRoom();
    });
  }

  ngOnDestroy(): void {
    // TODO: call leave player api.
  }

  /**
   * Joins the web-instance user to this room.
   */
  private joinRoom() {
    this.gameService.addPlayer(this.room.roomId)
      .subscribe(player => {
        this.player = player;
        console.log(this.player);
      });
  }

  getAllPlayers(): Object[] {
    return this.room.playersInRoom.concat(this.player);
  }
}
