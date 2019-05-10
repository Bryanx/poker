import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardComponent } from './card.component';
import {Card} from '../../model/card';

describe('CardComponent', () => {
  let component: CardComponent;
  let fixture: ComponentFixture<CardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should get the right image source', () => {
    const card: Card = new Card();

    card.type = 'ace_of_clubs';
    component.card = card;
    expect(component.getSrc()).toBe('/assets/img/cards/ace_of_clubs.svg');

    card.type = 'ace_of_SPADES';
    component.card = card;
    expect(component.getSrc()).toBe('/assets/img/cards/ace_of_spades.svg');
  });
});
