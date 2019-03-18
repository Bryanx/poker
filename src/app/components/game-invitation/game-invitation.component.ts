import { Component, OnInit } from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-game-invitation',
  templateUrl: './game-invitation.component.html',
  styleUrls: ['./game-invitation.component.scss'],
  animations: [
    trigger('simpleFadeAnimation', [
      state('in', style({opacity: 0.97})),
      transition(':enter', [
        style({opacity: 0}),
        animate(75)
      ]),
      transition(':leave',
        animate(75, style({opacity: 0})))
    ])
  ]
})
export class GameInvitationComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
