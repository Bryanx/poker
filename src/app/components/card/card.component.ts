import {Component, Input, OnInit} from '@angular/core';
import {Card} from '../../model/card';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {
  @Input() card: Card;

  constructor() {
  }

  ngOnInit() {
  }

  getSrc(): string {
    return '/assets/img/cards/' + this.card.type.toLowerCase() + '.svg';
  }
}
