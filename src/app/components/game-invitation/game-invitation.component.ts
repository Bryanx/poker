import {Component, Input, OnInit} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {forkJoin} from 'rxjs';
import {Friend} from '../../model/friend';
import {NotificationType} from '../../model/notificationType';
import {Notification} from '../../model/notification';
import {Room} from '../../model/room';

@Component({
  selector: 'app-game-invitation',
  templateUrl: './game-invitation.component.html',
  styleUrls: ['./game-invitation.component.scss'],
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
export class GameInvitationComponent implements OnInit {
  @Input() room: Room;
  friends: User[] = [];
  myself: User = User.create();
  loaded: Boolean = false;


  constructor(private userService: UserService) {
  }

  ngOnInit() {
    const ob1 = this.userService.getMyself();
    const ob2 = this.userService.getUsers();
    forkJoin(ob1, ob2).subscribe((bundle) => {
      this.myself = bundle[0];
      this.initializeFriends(bundle[0].friends, bundle[1]);
    });
  }

  initializeFriends(friends: Friend[], users: User[]) {
    this.friends = users.filter(user =>
      friends.filter(friend => friend.userId === user.id).length === 1
    );
    this.loaded = true;
  }

  /**
   * Sent a notification to someone for joining a game of poker.
   *
   * @param receiverId The id of the person that needs to receive the notification.
   */
  sendGameRequest(receiverId: string) {
    this.friends = this.friends.filter(friend => friend.id !== receiverId);
    const notification = new Notification();
    notification.message = this.myself.username + ' has sent you a request to join ' + this.room.name + ' room';
    notification.type = NotificationType.GAME_REQUEST;
    notification.ref = this.room.id as any as string;

    this.userService.sendNotification(receiverId, notification).subscribe();
  }
}
