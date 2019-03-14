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

  static create() {
    return {
      id: 0,
      cards: undefined,
      playersInRound: undefined,
      currentPhase: Phase.Not_Started,
      button: 0,
      isFinished: false,
      pot: 0
    };
  }
}
