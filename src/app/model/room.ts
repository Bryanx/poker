import {GameRules} from './gamerules';

export class Room {
  roomId: number;
  name: string;
  gameRules: GameRules;
  playersInRoom: Object[];
}
