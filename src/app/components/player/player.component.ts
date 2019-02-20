import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {
  @Input() userId: string;

  usePicture: Boolean = false;
  user: User = {
    id: '',
    username: '',
    firstname: '',
    lastname: '',
    chipsInRoom: 0,
    password: '',
    email: '',
    profilePicture: '',
    profilePictureSocial: '',
    provider: '',
    friends: [],
    chips: 0,
    wins: 0,
    gamesPlayed: 0,
    bestHand: ''
  };

  constructor(private userService: UserService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.userService.getUser(this.userId).subscribe(user => {
      this.user = user;
      if (this.user.profilePicture !== null) {
        this.usePicture = true;
      }
    });
  }

  getInitials(): string {
    return this.user.username.charAt(0).toUpperCase();
  }

  getProfilePicture() {
    if (this.user.profilePicture === null) {
      return this.user.profilePictureSocial;
    } else {
      return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.user.profilePicture);
    }
  }
}
