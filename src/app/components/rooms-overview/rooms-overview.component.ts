import {Component, OnInit} from '@angular/core';
import {Room} from '../../model/room';
import {GameService} from '../../services/game.service';
import {AuthorizationService} from '../../services/authorization.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BehaviorSubject, concat, forkJoin} from 'rxjs';
import {UserService} from '../../services/user.service';
import {PrivateRoom} from '../../model/privateRoom';
import {User} from '../../model/user';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-rooms-overview',
  templateUrl: './rooms-overview.component.html',
  styleUrls: ['./rooms-overview.component.scss'],
  animations: [
    trigger('simpleFadeAnimation', [
      state('in', style({opacity: 0.97})),
      transition(':enter', [
        style({opacity: 0}),
        animate(75)
      ]),
      transition(':leave',
        animate(75, style({opacity: 0})))
    ])
  ]
})
export class RoomsOverviewComponent implements OnInit {
  rooms = [];
  inSettingMode = false;
  public = false;
  showFriendModal: Boolean;
  users: User[];

  constructor(private gameService: GameService,
              private userService: UserService,
              private router: Router,
              private authService: AuthorizationService) {
  }

  ngOnInit() {
    this.getUsers();

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

  getUsers() {
    this.userService.getUsers().subscribe(users => this.users = users.filter(user => user.username.startsWith('j')));
  }
}
