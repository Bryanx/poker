import {GameRules} from './gamerules';
import {Player} from './player';

export class Room {
  id: number;
  name: string;
  gameRules: GameRules;
  playersInRoom: Player[];

  static create() {
    return {
      id: 0,
      name: 'test room',
      gameRules: undefined,
      playersInRoom: [Player.create()]
    };
  }
}
