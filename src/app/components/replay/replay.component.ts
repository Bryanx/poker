import { Component, OnInit } from '@angular/core';
import {Replay} from '../../model/replay';
import {RoomService} from '../../services/room.service';

@Component({
  selector: 'app-replay',
  templateUrl: './replay.component.html',
  styleUrls: ['./replay.component.scss']
})
export class ReplayComponent implements OnInit {
  replays: Replay[] = [];
  curReplay: number;

  constructor(private roomService: RoomService) { }

  ngOnInit() {
    this.curReplay = 0;
    this.roomService.getReplays().subscribe(replays => {
      console.log(replays);
      this.replays = replays;
    });
  }
}
