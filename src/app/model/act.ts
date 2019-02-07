import {ActType} from './actType';
import {Phase} from './phase';

export class Act {
  roundId: number;
  playerId: number;
  type: ActType;
  phase: Phase;
  bet: number;

}
