import {GameRules} from './gamerules';
import {Player} from './player';

export class Room {
  id: number;
  name: string;
  gameRules: GameRules;
  playersInRoom: Player[];
}
