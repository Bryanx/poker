export class CurrentPhaseBet {
  bet: number;
  userId: string;
  seatNumber: number;

  static create() {
    return {
      bet: 0,
      userId: 'blub',
      seatNumber: 99
    };
  }
}
