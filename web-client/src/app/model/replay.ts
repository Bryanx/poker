import {ReplayLine} from './replayLine';

export class Replay {
  id: number;
  roomName: string;
  roundNumber: number;
  lines: ReplayLine[];

  static create(): Replay {
    return {
      id: 0,
      roomName: '',
      roundNumber: 0,
      lines: []
    };
  }
}
