import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {DomSanitizer} from '@angular/platform-browser';
import {Player} from '../../model/player';
import {ActType} from '../../model/actType';
import {Act} from '../../model/act';
import {Phase} from '../../model/phase';
import {AuthorizationService} from '../../services/authorization.service';
import {Round} from '../../model/round';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {
  @Input() player: Player = Player.create();
  @Input() round: Round = Round.create();
  usePicture: Boolean = false;
  user: User = User.create();
  _currentAct: ActType;
  currentActStyle: string;

  constructor(private userService: UserService, private sanitizer: DomSanitizer, private authServie: AuthorizationService) { }

  ngOnInit() {
    this.userService.getUser(this.player.userId).subscribe(user => {
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

  @Input() set currentAct(act: Act) {
    if (act !== undefined) {
      if (act.phase === Phase.Showdown) {
        this._currentAct = ActType.Undecided;
      } else {
        if (act.userId === this.player.userId) {
          this._currentAct = act.type;
        }
      }
    }
  }

  whoAmI() {
    return this.authServie.getUserId() === this.user.id;
  }
}
