import {Friend} from './friend';

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
  friends: Friend[];

  static create() {
    return {
      id: '',
      username: '...',
      firstname: '',
      lastname: '',
      chipsInRoom: 0,
      password: '',
      email: '',
      profilePicture: '',
      profilePictureSocial: '',
      provider: '',
      friends: [],
      chips: 0,
      wins: 0,
      gamesPlayed: 0,
      bestHand: ''
    };
  }
}
