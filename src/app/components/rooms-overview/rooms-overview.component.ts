import {Component, OnInit} from '@angular/core';
import {Room} from '../../model/room';
import {AuthorizationService} from '../../services/authorization.service';
import {RoomService} from '../../services/room.service';

@Component({
  selector: 'app-rooms-overview',
  templateUrl: './rooms-overview.component.html',
  styleUrls: ['./rooms-overview.component.scss']
})
export class RoomsOverviewComponent implements OnInit {
  rooms: Room[] = [];

  constructor(private roomService: RoomService, private authService: AuthorizationService) {
  }

  ngOnInit() {
    this.roomService.getRooms().subscribe(rooms => {
      this.rooms = rooms;
    });
  }

  isAdmin() {
    return this.authService.isAdmin();
  }

  determineCapacityIcon(room: Room): string {
    const keyword: string = this.isFull(room) ? 'full' : 'not_full';
    return '../../../assets/img/icons/' + keyword + '.svg';
  }


  isFull(room: Room): boolean {
    return room.playersInRoom.length >= room.gameRules.maxPlayerCount;
  }
}
