import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthorizationService} from '../../services/authorization.service';
import {Router} from '@angular/router';
import {HomeVisibleService} from '../../services/home-visible.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

  constructor(public authorizationService: AuthorizationService, private router: Router, private homeObservable: HomeVisibleService) { }

  ngOnInit() {
   this.homeObservable.emitNewState(true);
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
}
