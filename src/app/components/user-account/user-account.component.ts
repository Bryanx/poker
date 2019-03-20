import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {switchMap} from 'rxjs/operators';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {DomSanitizer} from '@angular/platform-browser';
import {MatSnackBar} from '@angular/material';
import {NotificationType} from '../../model/notificationType';
import {Notification} from '../../model/notification';
import {Friend} from '../../model/friend';
import {Location} from '@angular/common';

@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.scss']
})
export class UserAccountComponent implements OnInit {
  myself: User = User.create();
  user: User = User.create();
  picture: String = './assets/img/icons/user.png';
  pending: Boolean = false;

  constructor(private userService: UserService,
              private route: ActivatedRoute,
              private sanitizer: DomSanitizer,
              private router: Router,
              private snackbar: MatSnackBar,
              private location: Location) {
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
    this.pending = true;
    const friend: Friend = new Friend();
    friend.userId = this.user.id;
    this.myself.friends.push(friend);
    this.userService.addFriend(friend).subscribe(() => {
      this.pending = false;
      this.sendFriendRequest(this.user.id);
      this.snackbar.open(this.user.username + ' was added as a friend.', '', {
        duration: 3000
      });
    });
  }

  /**
   * Sent a friend request to the requested user.
   *
   * @param receiverId The person who needs to receive the request.
   */
  private sendFriendRequest(receiverId: string) {
    const notification: Notification = new Notification();
    notification.type = NotificationType.FRIEND_REQUEST;
    notification.message = this.myself.username + ' has sent you a friend request!';
    notification.ref = this.myself.id;

    this.userService.sendNotification(receiverId, notification).subscribe();
  }

  removeFriend() {
    this.pending = true;
    this.myself.friends = this.myself.friends.filter(friend => friend.userId !== this.user.id);
    this.userService.deleteFriend(this.user.id).subscribe(() => {
      this.pending = false;
      this.snackbar.open(this.user.username + ' was removed as a friend.', '', {
        duration: 3000
      });
    });
  }

  isFriends() {
    return this.myself.friends.some(friend => friend.userId === this.user.id);
  }

  goBack() {
    return this.location.back();
  }

  settings() {
    return this.router.navigateByUrl('/settings');
  }

  myAccount() {
    return this.user.id === this.myself.id;
  }
}
