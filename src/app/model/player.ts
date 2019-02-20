import {Card} from './card';
import {ActType} from './actType';

export class Player {
  id: number;
  userId: string;
  firstCard: Card;
  secondCard: Card;
  lastAct: ActType;
  inRound: boolean;
  inRoom: boolean;
  chipCount: number;
  handType: string;
}
