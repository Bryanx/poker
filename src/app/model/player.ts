import {Card} from './card';

export class Player {
  id: number;
  userId: string;
  firstCard: Card;
  secondCard: Card;
  chipCount: number;
  handType: string;
}
