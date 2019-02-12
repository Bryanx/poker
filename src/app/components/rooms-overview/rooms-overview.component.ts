import {Component, OnInit} from '@angular/core';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {TranslateService} from '../../services/translate.service';

@Component({
  selector: 'app-rooms-overview',
  templateUrl: './rooms-overview.component.html',
  styleUrls: ['./rooms-overview.component.scss']
})
export class RoomsOverviewComponent implements OnInit {
  rooms: Room[] = [];

  constructor(private gameService: GameService) {
  }

  ngOnInit() {
    this.gameService.getRooms().subscribe(rooms => {
      this.rooms = rooms;
    });
  }

  determineCapacityIcon(room: Room): string {
    const keyword: string = this.isFull(room) ? 'full' : 'not_full';
    return '../../../assets/img/icons/' + keyword + '.svg';
  }


  isFull(room: Room): boolean {
    return room.playersInRoom.length >= room.gameRules.maxPlayerCount;
  }
}
