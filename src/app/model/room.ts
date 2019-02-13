import {GameRules} from './gamerules';
import {Player} from './player';

export class Room {
  roomId: number;
  name: string;
  gameRules: GameRules;
  playersInRoom: Player[];
}
