import {Component, OnInit} from '@angular/core';
import {AuthorizationService} from '../../services/authorization.service';
import {Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {PrivateRoom} from '../../model/privateRoom';
import {User} from '../../model/user';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {WhiteListedUser} from '../../model/whiteListedUser';
import {Notification} from '../../model/notification';
import {NotificationType} from '../../model/notificationType';
import {RoomService} from '../../services/room.service';

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
  myself: User;
  whiteListedUsers: User[];
  nonWhiteListedUsers: User[];
  curRoom: PrivateRoom;
  dataLoaded: Boolean;
  isPrivate: Boolean = false;

  constructor(private roomService: RoomService,
              private userService: UserService,
              private router: Router,
              private authService: AuthorizationService) {
  }

  ngOnInit() {
    this.getMyself();
    const url: string = this.router.url;

    if (url.includes('private')) {
      if (url.includes('settings')) {
        this.roomService.getPrivateRoomsFromOwner().subscribe(rooms => this.rooms = rooms);
        this.getUsers();
        this.inSettingMode = true;
        this.isPrivate = true;
      } else {
        this.roomService.getPrivateRooms().subscribe(rooms => this.rooms = rooms);
        this.isPrivate = true;
      }
    } else {
      this.public = true;
      this.roomService.getRooms().subscribe(rooms => this.rooms = rooms);
    }
  }



  isAdmin() {
    return this.authService.isAdmin();
  }

  loadCorrespondingData(room: PrivateRoom) {
    if (this.curRoom === undefined || this.curRoom.id !== room.id) {
      this.curRoom = room;
    }

    this.whiteListedUsers = this.users.filter(user => this.isInWhiteList(user.id, this.curRoom.whiteListedUsers));
    this.nonWhiteListedUsers = this.users.filter(user => !this.isInWhiteList(user.id, this.curRoom.whiteListedUsers));
  }

  addToWhiteList(user: User) {
    this.toggleWhiteListedUser(user, true);
    this.roomService.addToWhiteList(this.curRoom.id, user.id).subscribe(room => {
      this.curRoom = room;
      this.refreshData();
    });
    this.notifyUser(user, true);
  }

  deleteFromWhiteList(user: User) {
    this.toggleWhiteListedUser(user, false);
    this.roomService.deleteFromWhiteList(this.curRoom.id, user.id).subscribe(room => {
      this.curRoom = room;
      this.refreshData();
    });
    this.notifyUser(user, false);
  }

  private refreshData() {
    this.roomService.getPrivateRoomsFromOwner().subscribe(rooms => this.rooms = rooms);
  }

  private getUsers() {
    this.userService.getUsers().subscribe(users => {
      this.users = users;
      this.dataLoaded = true;
    });
  }

  private getMyself() {
    this.userService.getMyself().subscribe(me => this.myself = me);
  }

  private isInWhiteList(userId: string, whiteListedUsers: WhiteListedUser[]) {
    for (let i = 0; i < whiteListedUsers.length; i++) {
      if (whiteListedUsers[i].userId === userId) {
        return true;
      }
    }
    return false;
  }

  private toggleWhiteListedUser(user: User, add: boolean) {
    if (add) {
      const index = this.nonWhiteListedUsers.indexOf(user);
      this.nonWhiteListedUsers.splice(index, 1);
      this.whiteListedUsers.push(user);
    } else {
      const index = this.whiteListedUsers.indexOf(user);
      this.whiteListedUsers.splice(index, 1);
      this.nonWhiteListedUsers.push(user);
    }
  }

  private notifyUser(user: User, added: boolean) {
    const not: Notification = new Notification();
    not.ref = this.myself.id;
    not.message = this.myself.username + ' has ' + (added ? 'added' : 'deleted') + ' you ' +
      (added ? 'to' : 'from') + ' ' + this.curRoom.name + '!';
    not.type = added ? NotificationType.ADD_PRIVATE_ROOM : NotificationType.DELETE_PRIVATE_ROOM;

    this.userService.sendNotification(user.id, not).subscribe();
  }
}
