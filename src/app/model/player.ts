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
  seatNumber: number;
  access_token: string;

  static create() {
    return {
      id: 0,
      userId: '0',
      firstCard: undefined,
      secondCard: undefined,
      lastAct: undefined,
      inRound: true,
      inRoom: true,
      chipCount: 5000,
      handType: 'BAD',
      seatNumber: 1,
      access_token: 'ojfzeoio'
    };
  }
}

