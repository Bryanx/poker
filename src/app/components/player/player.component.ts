import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {DomSanitizer} from '@angular/platform-browser';
import {Player} from '../../model/player';
import {ActType} from '../../model/actType';
import {Act} from '../../model/act';
import {Phase} from '../../model/phase';
import {GameRules} from '../../model/gamerules';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {
  @Input() player: Player = Player.create();
  usePicture: Boolean = false;
  user: User = User.create();
  _currentAct: ActType;
  currentActStyle: string;
  @Input() gameRules: GameRules;
  counter: number;
  timerInterval: any;
  progressBarCounter = 20;

  constructor(private userService: UserService, private sanitizer: DomSanitizer, private cdRef: ChangeDetectorRef) {
  }

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

  setTimer() {
    this.counter = this.gameRules.playDelay;
    this.timerInterval = setInterval(() => {
      if (this.counter > 0) {
        this.counter -= 1;
        this.progressBarCounter = this.counter / this.gameRules.playDelay * 100;
        console.log(this.progressBarCounter);
      } else {
        clearInterval(this.timerInterval);
        console.log('fold');
      }
    }, 1000);
  }
}
