import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthorizationService} from '../../services/authorization.service';
import {Router} from '@angular/router';
import {HomeVisibleService} from '../../services/home-visible.service';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  myself: User = User.create();

  constructor(public authorizationService: AuthorizationService, private router: Router, private homeObservable: HomeVisibleService, private userService: UserService) { }

  ngOnInit() {
   this.homeObservable.emitNewState(true);
   this.homeObservable.emitHome(true);
   this.getMyself();
  }

  ngOnDestroy(): void {
    this.homeObservable.emitNewState(false);
  }

  isAuthenticated() {
    return this.authorizationService.isAuthenticated();
  }

  isAdmin() {
    return this.authorizationService.isAdmin();
  }

  logout() {
    this.authorizationService.logout();
    this.router.navigateByUrl('/');
  }

  getMyself() {
    this.userService.getMyself().subscribe(user => {
      this.myself = user;
      })
  }
}
