export class User {
  id: string;
  username: string;
  firstname: string;
  lastname: string;
  chipsInRoom: number;
  email: string;
  password: string;
  profilePicture: string;
  profilePictureSocial: string;
  provider: string;
  chips: number;
  wins: number;
  gamesPlayed: number;
  bestHand: string;
  friends: User[] = [];
}
