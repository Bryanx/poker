import {Component, Input} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-room-card',
  templateUrl: './room-card.component.html',
  styleUrls: ['./room-card.component.scss'],
  animations: [
    trigger('simpleFadeAnimation', [
      state('in', style({opacity: 1})),
      transition(':enter', [
        style({opacity: 0}),
        animate(350)
      ])
    ])
  ]
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
