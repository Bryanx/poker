import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UrlService {
  userServiceUrl = 'https://poker-user-service.herokuapp.com/api/user';
  // userServiceUrl = 'http://localhost:5000/api/user';

  gameServiceUrl = 'https://poker-game-service.herokuapp.com/api/rooms';
  // gameServiceUrl = 'http://localhost:5001/api/rooms';

  socialUrl = 'https://poker-user-service.herokuapp.com/api/sociallogin';
  // socialUrl = 'http://localhost:5000/api/sociallogin';

  authUrl = 'https://poker-user-service.herokuapp.com/oauth/token';
  // authUrl = 'http://localhost:5000/oauth/token';
}
