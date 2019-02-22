import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged, switchMap} from 'rxjs/operators';
import {Notification} from '../../model/notification';
import {NotificationType} from '../../model/notificationType';
import {AuthorizationService} from '../../services/authorization.service';
import {RxStompService} from '@stomp/ng2-stompjs';

/**
 * This component will be used for searching through all the users
 * that are in the system.
 */
@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  private debounceTime: Number = 400;
  users: User[] = [];
  inputString: String = '';
  subject: Subject<String> = new Subject();
  myself: User = User.create();

  constructor(private userService: UserService, private webSocketService: RxStompService) {
  }

  /**
   * When the component is created, the functionality will be piped into the subject and be subscribed on.
   * A subject is an observable stream were you can put data inside it and manipulate it.
   * This subject is defined as a string, which means
   * that the stream will only accept strings as input.
   */
  ngOnInit(): void {
    this.userService.getMyself().subscribe(user => this.myself = user);
    this.userService.getUsers().subscribe(users => this.users = users);

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
    this.subject.next(this.inputString);
  }

  /**
   * Adds a friend to the current user.
   *
   * @param friend The friend that needs to be added.
   */
  addFriend(friend: User) {
    this.myself.friends.push(friend);
    this.userService.changeUser(this.myself).subscribe();
    this.sendFriendRequest(friend.id);
  }

  private sendFriendRequest(receiverId: string) {
    const notification: Notification = new Notification();
    notification.type = NotificationType.FRIEND_REQUEST;
    notification.sender = this.myself;
    notification.message = this.myself.username + ' has sent you a friend request!';
    console.log(notification);

    this.webSocketService.publish({
      destination: '/user/' + receiverId + '/send-notification',
      body: JSON.stringify(JSON.stringify(notification))
    });
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
      return !this.myself.friends.some(friend => friend.id === user.id);
    }
  }
}
