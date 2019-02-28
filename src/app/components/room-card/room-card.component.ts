import {Component, EventEmitter, Input, Output} from '@angular/core';
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
  @Input() inSettingMode;
  @Input() isAdmin;

  determineCapacityIcon(): string {
    const keyword: string = this.isFull() ? 'full' : 'not_full';
    return '../../../assets/img/icons/' + keyword + '.svg';
  }

  determineSecondClass() {
    if (this.inSettingMode) {
      return 'enabled-settings';
    } else if (this.isFull()) {
      return 'disabled';
    } else {
      return 'enabled';
    }
  }

  isFull(): boolean {
    return this.room.playersInRoom.length >= this.room.gameRules.maxPlayerCount;
  }

  determineRouterLink() {
    if (this.isAdmin) {
      return '/game-rooms/';
    } else if (this.inSettingMode) {
      return '/rooms/private/edit/';
    } else {
      return '/rooms/';
    }
  }
}
