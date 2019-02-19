import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {Observable, Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged, map, switchMap} from 'rxjs/operators';
import {DomSanitizer} from '@angular/platform-browser';
import {AuthorizationService} from '../../services/authorization.service';
import { EMPTY } from 'rxjs';

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

  @Output() friendAdded: EventEmitter<User> = new EventEmitter();

  constructor(private userService: UserService, private sanitizer: DomSanitizer) {
  }

  /**
   * When the component is created, the functionality will be piped into the subject and be subscribed on.
   * A subject is an observable stream were you can put data inside it and manipulate it.
   * This subject is defined as a string, which means that the stream will only accept strings as input.
   */
  ngOnInit(): void {
    this.subject.pipe(
      debounceTime(this.debounceTime as number),
      distinctUntilChanged(),
    ).subscribe(value => {
      this.userService.getUsersByName(value as string).subscribe(users => {
        if (!this.inputString) {
          this.users = [];
        } else {
          this.users = users;
        }
      });
    });
  }

  /**
   * Gives back the profile picture of the users. If there no picture, a null
   * value will be returned.
   *
   * @param user The user with the possible picture.
   */
  getProfilePicture(user: User) {
    if (user.profilePicture === null) {
      return user.profilePictureSocial;
    } else {
      return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + user.profilePicture);
    }
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
}
