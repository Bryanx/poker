import {Phase} from './phase';
import {Card} from './card';
import {Player} from './player';

export class Round {
  id: number;
  cards: Card[];
  playersInRound: Player[];
  currentPhase: Phase;
  button: number;
  isFinished: boolean;
  pot: number;
}
