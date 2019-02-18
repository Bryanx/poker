import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged} from 'rxjs/operators';
import {DomSanitizer} from '@angular/platform-browser';

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

  constructor(private userService: UserService, private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.subject.pipe(
      debounceTime(this.debounceTime as number),
      distinctUntilChanged()
    ).subscribe(value => this.getUsers(value as string));
    this.getUsers();
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

  private getUsers(input: string = ''): void {
    if (input === '') {
      this.userService.getUsers().subscribe(users => this.users = users);
    } else {
      this.userService.getUsersByName(input).subscribe(users => this.users = users);
    }
  }
}
