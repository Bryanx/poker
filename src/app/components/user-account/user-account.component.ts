import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {switchMap} from 'rxjs/operators';
import {ActivatedRoute, ParamMap} from '@angular/router';

@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.scss']
})
export class UserAccountComponent implements OnInit {
  user: User = {
    id: '',
    username: '',
    firstname: '',
    lastname: '',
    chipcount: 0,
    password: '',
    email: '',
    profilePicture: '',
    profilePictureSocial: '',
    provider: '',
    friends: []
  };


  constructor(private userService: UserService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.userService.getUser(params.get('id'));
    })).subscribe(user => this.user = user);
  }
}
