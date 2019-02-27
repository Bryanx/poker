import {GameRules} from './gamerules';
import {Player} from './player';

export class PrivateRoom {
  id: number;
  name: string;
  gameRules: GameRules;
  playersInRoom: Player[];
  ownerUserId: string;

  static create() {
    return {
      id: 0,
      name: 'test room',
      gameRules: undefined,
      playersInRoom: [Player.create()],
      ownerUserId: ''
    };
  }
}
