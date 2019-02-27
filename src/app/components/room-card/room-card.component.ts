import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-room-card',
  templateUrl: './room-card.component.html',
  styleUrls: ['./room-card.component.scss']
})
export class RoomCardComponent {
  @Input() room;

  determineCapacityIcon(): string {
    const keyword: string = this.isFull() ? 'full' : 'not_full';
    return '../../../assets/img/icons/' + keyword + '.svg';
  }

  isFull(): boolean {
    return this.room.playersInRoom.length >= this.room.gameRules.maxPlayerCount;
  }
}
