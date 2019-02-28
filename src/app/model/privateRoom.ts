import {GameRules} from './gamerules';
import {Player} from './player';
import {WhiteListedUser} from './whiteListedUser';

export class PrivateRoom {
  id: number;
  name: string;
  gameRules: GameRules;
  playersInRoom: Player[];
  ownerUserId: string;
  whiteListedUsers: WhiteListedUser[];

  static create() {
    return {
      id: 0,
      name: 'test room',
      gameRules: undefined,
      playersInRoom: [Player.create()],
      ownerUserId: '',
      whitelistedUsers: [WhiteListedUser.create()]
    };
  }
}
