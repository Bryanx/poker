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
  users: User[] = [];
  inputString: String = '';
  subject: Subject<String> = new Subject();

  constructor(private userService: UserService, private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.subject.pipe(
      debounceTime(400),
      distinctUntilChanged()
    ).subscribe(value => {
      this.getUsers(value as string);
      console.log(this.users);
    });
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
    const words: string[] = user.username.split(' ');
    const initials: string[] = [];

    for (let i = 0; i < words.length; i++) {
      initials.push(words[i].charAt(0).toUpperCase());
    }

    return initials.join('');
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
