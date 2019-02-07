import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {switchMap} from 'rxjs/operators';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {Round} from '../../model/round';

@Component({
  selector: 'app-room',
  templateUrl: './game-room.component.html',
  styleUrls: ['./game-room.component.scss']
})
export class GameRoomComponent implements OnInit {
  room: Room;
  curRound: Round;

  constructor(private router: ActivatedRoute, private gameService: GameService) { }

  ngOnInit() {
    this.router.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.gameService.getRoom(+params.get('id'));
    })).subscribe((room) => {
      this.room = room as Room;
      this.retrieveRound(this.room.roomId);
    });
  }

  // TODO: find a way to trigger the round retrieval if there are more than 2 players in the room.
  private retrieveRound(roomId: number) {
    this.gameService.getCurrentRound(roomId).subscribe(round => {
      this.curRound = round;
      console.log(this.curRound);
    });
  }
}
