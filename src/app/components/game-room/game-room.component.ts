import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
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

  constructor(private curRouter: ActivatedRoute, private router: Router, private gameService: GameService) {
  }

  ngOnInit() {
    this.curRouter.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.gameService.getRoom(+params.get('id'));
    })).subscribe((room) => {
      console.log(this.player);
      this.room = room as Room;

      if (this.room.playersInRoom.length >= this.room.gameRules.maxPlayerCount) {
        this.router.navigateByUrl('/rooms');
      }

      this.joinRoom();
    });
  }

  ngOnDestroy(): void {
    this.gameService.deletePlayer(this.room.roomId).subscribe();
  }

  /**
   * Joins the web-instance user to this room.
   */
  private joinRoom() {
    this.gameService.addPlayer(this.room.roomId)
      .subscribe(player => {
        this.player = player;
      });
  }

  getAllPlayers(): Object[] {
    return this.room.playersInRoom.concat(this.player);
  }
}
