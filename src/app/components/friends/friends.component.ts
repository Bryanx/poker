import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {forkJoin} from 'rxjs';
import {Friend} from '../../model/friend';

/**
 * Component for displaying friends
 */
@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.scss'],
  animations: [
    trigger('simpleFadeAnimation', [
      state('in', style({opacity: 1})),
      transition(':enter', [
        style({opacity: 0}),
        animate(200)
      ]),
    ])
  ]
})
export class FriendsComponent implements OnInit {
  myself: User = User.create();
  friends: User[] = [];

  constructor(private userService: UserService) { }

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
  }

  removeFriend(userId: string) {
    this.myself.friends = this.myself.friends.filter(friend => friend.userId !== userId);
    this.friends = this.friends.filter(user => user.id !== userId);
    this.userService.changeUser(this.myself).subscribe();
  }
}
