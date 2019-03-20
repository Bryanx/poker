import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged, switchMap} from 'rxjs/operators';
import {Notification} from '../../model/notification';
import {NotificationType} from '../../model/notificationType';
import {AuthorizationService} from '../../services/security/authorization.service';
import {Router} from '@angular/router';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Friend} from '../../model/friend';

/**
 * This component will be used for searching through all the users
 * that are in the system.
 */
@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
  animations: [
    trigger('simpleFadeAnimation', [
      state('in', style({opacity: 1})),
      transition(':enter', [
        style({opacity: 0}),
        animate(90)
      ]),
      transition(':leave',
        animate(90, style({opacity: 0})))
    ])
  ]
})
export class SearchComponent implements OnInit {
  private debounceTime: Number = 400;
  users: User[] = [];
  admins: User[] = [];
  typed: Boolean = false;
  inputString: String = '';
  subject: Subject<String> = new Subject();
  myself: User = User.create();

  constructor(private userService: UserService, private authService: AuthorizationService, private router: Router) {
  }

  /**
   * When the component is created, the functionality will be piped into the subject and be subscribed on.
   * A subject is an observable stream were you can put data inside it and manipulate it.
   * This subject is defined as a string, which means
   * that the stream will only accept strings as input.
   */
  ngOnInit(): void {
    if (this.isAdmin()) {
      this.updateUsers();
    }

    this.subject.pipe(
      debounceTime(this.debounceTime as number),
      distinctUntilChanged(),
      switchMap(() => this.userService.getUsersByName(this.inputString as string))
    ).subscribe(users => {
      if (!this.inputString) {
        this.users = [];
      } else {
        this.users = users;
      }
    });
  }

  /**
   * Gives back the first letter in upper case as initial of the user.
   *
   * @param user The user we need to get the initials from.
   */
  getInitials(user: User): string {
    return user.username.charAt(0).toUpperCase();
  }

  /**
   * Adds the input string that is two-way-bind to the input-field to the subject.
   */
  addToSubject(): void {
    this.typed = true;
    this.subject.next(this.inputString);
  }

  /**
   * Adds a friend to the current user.
   *
   * @param friendId The friend that needs to be added.
   */
  addFriend(friendId: string) {
    const friend: Friend = new Friend();
    friend.userId = friendId;
    this.myself.friends.push(friend);

    this.userService.changeFriends(this.myself).subscribe(() =>  this.sendFriendRequest(friendId));
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

  /**
   * Checks if we need to show a button to befriend someone.
   *
   * @param user The user where the checks need to happen on.
   */
  checkShowButton(user: User) {
    if (user.id === this.myself.id) {
      return false;
    } else {
      return !this.myself.friends.some(friend => friend.userId === user.id);
    }
  }

  isAdmin() {
    return this.authService.isAdmin();
  }

  adminDisable(user: User) {
    if (user.enabled === 1) {
      user.enabled = 0;
    } else {
      user.enabled = 1;
    }
    this.userService.disableUser(user).subscribe();
  }

  makeAdmin(user: User) {
    this.userService.changeToAdmin(user).subscribe();
    this.ngOnInit();
  }

  makeUser(user: User) {
    this.userService.changeToUser(user).subscribe();
    this.updateUsers();
  }

  private updateUsers() {
    this.userService.getMyself().subscribe(user => this.myself = user);
    if (this.isAdmin()) {
      this.userService.getUsers().subscribe(users => this.users = users);
      this.userService.getAdmins().subscribe(admins => this.admins = admins);
    } else {
      this.userService.getUsers().subscribe(users => this.users = users);
    }
  }

  stop(event: Event) {
    event.stopPropagation();
  }
}
