import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {switchMap} from 'rxjs/operators';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {DomSanitizer} from '@angular/platform-browser';
import {Location} from '@angular/common';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.scss']
})
export class UserAccountComponent implements OnInit {
  myself: User;
  user: User = User.create();
  picture: String = './assets/img/icons/user.png';

  constructor(private userService: UserService,
              private route: ActivatedRoute,
              private sanitizer: DomSanitizer,
              private location: Location,
              private snackbar: MatSnackBar) {
  }

  ngOnInit() {
    this.userService.getMyself().subscribe(me => this.myself = me);
    this.route.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.userService.getUser(params.get('id'));
    })).subscribe(user => {
      this.user = user;
      this.picture = this.getProfilePicture();
    });
  }

  getProfilePicture() {
    if (this.user.profilePicture === null) {
      return this.user.profilePictureSocial;
    } else {
      return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.user.profilePicture) as string;
    }
  }

  setDefaultPicture() {
    this.picture = './assets/img/icons/user.png';
  }

  addFriend() {
    this.myself.friends.push(this.user);
    this.userService.changeUser(this.myself).subscribe(() => {
      this.snackbar.open(this.user.username + ' was added as a friend.', '', {
        duration: 3000
      });
    });
  }

  removeFriend() {
    this.myself.friends = this.myself.friends.filter(friend => friend.id !== this.user.id);
    this.userService.changeUser(this.myself).subscribe(() => {
      this.snackbar.open(this.user.username + ' was removed as a friend.', '', {
        duration: 3000
      });
    });
  }

  isFriends() {
    return this.myself.friends.some(friend => friend.id === this.user.id);
  }
}
