import {Component, OnInit} from '@angular/core';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {AuthorizationService} from '../../services/authorization.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BehaviorSubject, concat, forkJoin} from 'rxjs';
import {UserService} from '../../services/user.service';
import {PrivateRoom} from '../../model/privateRoom';
import {User} from '../../model/user';

@Component({
  selector: 'app-rooms-overview',
  templateUrl: './rooms-overview.component.html',
  styleUrls: ['./rooms-overview.component.scss']
})
export class RoomsOverviewComponent implements OnInit {
  rooms = [];
  inSettingMode = false;

  constructor(private gameService: GameService,
              private userService: UserService,
              private router: Router,
              private authService: AuthorizationService) {
  }

  ngOnInit() {
    const url: string = this.router.url;

    if (url.includes('private')) {
      if (url.includes('settings')) {
        this.gameService.getPrivateRoomsFromOwner().subscribe(rooms => this.rooms = rooms);
        this.inSettingMode = true;
      } else {
        this.gameService.getPrivateRooms().subscribe(rooms => this.rooms = rooms);
      }

      /*
      const ob1 = this.gameService.getPrivateRooms();
      const ob2 = this.userService.getMyself();
      forkJoin(ob1, ob2).subscribe(roomsuser => {
        const rooms: PrivateRoom[] = roomsuser[0] as PrivateRoom[];
        const myself: User = roomsuser[1] as User;

        if (url.includes('settings')) {
          console.log(rooms);

        } else {
          this.rooms = rooms;
        }
      });
      */
    } else {
      this.gameService.getRooms().subscribe(rooms => this.rooms = rooms);
    }
  }

  isAdmin() {
    return this.authService.isAdmin();
  }
}
