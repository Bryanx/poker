import { Component, OnInit } from '@angular/core';
import {AuthorizationService} from '../../services/authorization.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(public authorizationService: AuthorizationService) { }

  ngOnInit() {
  }

  isAuthenticated() {
    return this.authorizationService.isAuthenticated();
  }
}
