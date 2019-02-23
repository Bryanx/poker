export class Player {
  id: number;
  userId: string;
  chipCount: number;
  handType: string;
  inRoom: boolean;

  static create() {
    return {
      id: 1,
      userId: '1',
      inRoom: false,
      handType: '',
      chipCount: 0
    };
  }
}

