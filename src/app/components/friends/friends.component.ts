import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';

/**
 * Component for displaying/searching friends
 */
@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.scss']
})
export class FriendsComponent implements OnInit {
  myself: User;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getMyself().subscribe(me => this.myself = me);
  }

  removeFriend(friend: User) {
    this.myself.friends = this.myself.friends.filter(other => other !== friend);
    this.userService.changeUser(this.myself).subscribe();
  }
}
