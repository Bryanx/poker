import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {PrivateRoom} from '../../model/privateRoom';
import {Room} from '../../model/room';
import {GameRules} from '../../model/gamerules';

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
  @Input() room = Room.create();
  @Input() inSettingMode;
  @Input() isAdmin;
  @Input() dataLoaded;
  @Input() isPrivate;
  @Input() me;

  @Output() modalEvent: EventEmitter<Boolean> = new EventEmitter();
  @Output() roomEvent: EventEmitter<PrivateRoom> = new EventEmitter();

  determineCapacityIcon(): string {
    const keyword: string = this.isFull() ? 'full' : 'not_full';
    return '../../../assets/img/icons/' + keyword + '.svg';
  }

  determineSecondClass() {
    if (this.inSettingMode) {
      return 'enabled-settings';
    } else if (this.isFull() || this.me.level < this.room.gameRules.minLevel ) {
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

  showFriendModal() {
    this.modalEvent.emit(true);
    this.roomEvent.emit(this.room as PrivateRoom);
  }
}
