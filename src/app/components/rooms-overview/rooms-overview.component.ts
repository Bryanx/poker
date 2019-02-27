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
  public = false;

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
    } else {
      this.public = true;
      this.gameService.getRooms().subscribe(rooms => this.rooms = rooms);
    }
  }

  isAdmin() {
    return this.authService.isAdmin();
  }

  determineRouterLink() {
    if (this.isAdmin()) {
      return '/game-rooms/';
    } else if (this.inSettingMode) {
      return '/rooms/private/edit/';
    } else {
      return '/rooms/';
    }
  }
}
