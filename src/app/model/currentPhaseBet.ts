import {Phase} from './phase';

export class CurrentPhaseBet {
  bet: number;
  userId: string;
  seatNumber: number;
  phase: Phase;

  static create() {
    return {
      bet: 0,
      userId: '0000',
      seatNumber: 99,
      phase: Phase.Not_Started
    };
  }

  static createArrayOfSix() {
    return [
      {
        bet: 0,
        userId: '0000',
        seatNumber: 1,
        phase: Phase.Not_Started
      },
      {
        bet: 0,
        userId: '0000',
        seatNumber: 2,
        phase: Phase.Not_Started
      },
      {
        bet: 0,
        userId: '0000',
        seatNumber: 3,
        phase: Phase.Not_Started
      },
      {
        bet: 0,
        userId: '0000',
        seatNumber: 4,
        phase: Phase.Not_Started
      },
      {
        bet: 0,
        userId: '0000',
        seatNumber: 5,
        phase: Phase.Not_Started
      },
      {
        bet: 0,
        userId: '0000',
        seatNumber: 6,
        phase: Phase.Not_Started
      }
    ];
  }
}
