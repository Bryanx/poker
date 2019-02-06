import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {
  @Input() smallAndBigBlind: String;
  @Input() capacity: Number;

  constructor() { }

  ngOnInit() {
  }

}
