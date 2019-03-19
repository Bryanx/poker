import { Component, OnInit } from '@angular/core';
import {Replay} from '../../model/replay';
import {RoomService} from '../../services/room.service';

@Component({
  selector: 'app-replay',
  templateUrl: './replay.component.html',
  styleUrls: ['./replay.component.scss']
})
export class ReplayComponent implements OnInit {
  replays: Replay[] = [Replay.create()];
  curReplay: number;

  constructor(private roomService: RoomService) { }

  ngOnInit() {
    this.curReplay = 0;
    this.roomService.getReplays().subscribe(replays => this.replays = replays);
  }

  switchPage(val: number) {
    if (this.curReplay + val < 0) {
      this.curReplay = this.replays.length - 1;
    } else if (this.curReplay + val > this.replays.length - 1) {
      this.curReplay = 0;
    } else {
      this.curReplay += val;
    }
  }
}
