import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {
  @Input() userId: number;
  user: User = {
    id: '',
    username: '',
    firstname: '',
    lastname: '',
    password: '',
    email: '',
    profilePicture: '',
    profilePictureSocial: '',
    provider: ''
  };

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getUser().subscribe(user => this.user = user)
  }

  getInitials(): string {
    const words: string[] = this.user.username.split(' ');
    const initials: string[] = [];

    for (let i = 0; i < words.length; i++) {
      initials.push(words[i].charAt(0).toUpperCase());
    }

    return initials.join('');
  }
}
