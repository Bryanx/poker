import { Component, OnInit } from '@angular/core';
import {AuthorizationService} from '../../services/authorization.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(public authorizationService: AuthorizationService, private router: Router) { }

  ngOnInit() {
  }

  isAuthenticated() {
    return this.authorizationService.isAuthenticated();
  }

  logout() {
    this.authorizationService.logout();
    this.router.navigateByUrl('/');
  }
}
