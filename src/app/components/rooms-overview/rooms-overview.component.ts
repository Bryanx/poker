import {Component, OnInit} from '@angular/core';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {AuthorizationService} from '../../services/authorization.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BehaviorSubject} from 'rxjs';

@Component({
  selector: 'app-rooms-overview',
  templateUrl: './rooms-overview.component.html',
  styleUrls: ['./rooms-overview.component.scss']
})
export class RoomsOverviewComponent implements OnInit {
  rooms = [];

  constructor(private gameService: GameService,
              private router: Router,
              private authService: AuthorizationService) {
  }

  ngOnInit() {
    const url: string = this.router.url;

    if (!url.includes('private')) {
      this.gameService.getRooms().subscribe(rooms => {
        this.rooms = rooms;
      });
    } else {
      this.gameService.getPrivateRooms().subscribe(rooms => {
        this.rooms = rooms;
        console.log(rooms);
      });
    }
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
