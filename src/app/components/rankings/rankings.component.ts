import { Component, OnInit } from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-rankings',
  templateUrl: './rankings.component.html',
  styleUrls: ['./rankings.component.scss'],
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
export class RankingsComponent implements OnInit {
  users: User[] = [];
  displayedColumns = ['rank', 'username', 'level', 'chips', 'wins', 'lost', 'gamesPlayed'];

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getUsers().subscribe(users => {
      this.users = users.sort((o1, o2) => o2.chips - o1.chips);
    });
  }
}
