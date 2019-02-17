import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged} from 'rxjs/operators';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  users: User[] = [];
  inputString: String = '';
  subject: Subject<String> = new Subject();

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.subject.pipe(
      debounceTime(400),
      distinctUntilChanged()
    ).subscribe(value => this.getUsers(value as string));
    this.getUsers();
  }

  changeSubject(): void {
    this.subject.next(this.inputString);
  }

  private getUsers(input: string = ''): void {
    if (input === '') {
      this.userService.getUsers().subscribe(users => {
        this.users = users;
        console.log(this.users);
      });
    } else {
      this.userService.getUsersByName(input).subscribe(users => {
        this.users = users;
        console.log(this.users);
      });
    }
  }
}
