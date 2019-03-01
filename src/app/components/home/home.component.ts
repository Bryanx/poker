import { Component, OnInit } from '@angular/core';
import {AuthorizationService} from '../../services/authorization.service';
import {Router} from '@angular/router';
import {WebSocketService} from '../../services/web-socket.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(public authorizationService: AuthorizationService, private router: Router, private webSocketService: WebSocketService) { }

  ngOnInit() {
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
