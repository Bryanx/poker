import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {animate, state, style, transition, trigger} from '@angular/animations';

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

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getMyself().subscribe(me => this.myself = me);
  }

  removeFriend(friend: User) {
    this.myself.friends = this.myself.friends.filter(other => other !== friend);
    this.userService.changeUser(this.myself).subscribe();
  }
}
