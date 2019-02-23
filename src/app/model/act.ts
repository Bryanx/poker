import {ActType} from './actType';
import {Phase} from './phase';

export class Act {
  roundId: number;
  userId: string;
  playerId: number;
  roomId: number;
  type: ActType;
  phase: Phase;
  bet: number;
  nextUserId: string;
}
