export class WhiteListedUser {
  id: number;
  userId: string;

  static create() {
    return {
      id: 0,
      userId: ''
    };
  }
}
