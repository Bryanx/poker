import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {Observable, Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged, map, switchMap} from 'rxjs/operators';
import {DomSanitizer} from '@angular/platform-browser';
import {AuthorizationService} from '../../services/authorization.service';
import { EMPTY } from 'rxjs';

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

  getProfilePicture(user: User) {
    if (user.profilePicture === null) {
      return user.profilePictureSocial;
    } else {
      return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + user.profilePicture);
    }
  }

  getInitials(user: User): string {
    return user.username.charAt(0).toUpperCase();
  }

  changeSubject(): void {
    this.subject.next(this.inputString);
  }
}
