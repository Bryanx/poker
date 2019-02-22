import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {DomSanitizer} from '@angular/platform-browser';
import {Player} from "../../model/player";

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {
  @Input() userId: string;
  @Input() player: Player;

  usePicture: Boolean = false;
  user: User = User.create();

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
