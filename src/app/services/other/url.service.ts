import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UrlService {
  userServiceUrl = 'https://poker-user-service.herokuapp.com/api/user';
  // userServiceUrl = 'http://localhost:5000/api/user';

  socialUrl = 'https://poker-user-service.herokuapp.com/api/sociallogin';
  // socialUrl = 'http://localhost:5000/api/sociallogin';

  authUrl = 'https://poker-user-service.herokuapp.com/oauth/token';
  // authUrl = 'http://localhost:5000/oauth/token';

  roundUrl = 'https://poker-game-service.herokuapp.com/api/rounds';
  // roundUrl = 'http://localhost:5001/api/rounds';

  roomUrl = 'https://poker-game-service.herokuapp.com/api/rooms';
  // roomUrl = 'http://localhost:5001/api/rooms';

  gameServiceWebSocketUrl = 'wss://poker-game-service.herokuapp.com/connect/websocket';
  // gameServiceWebSocketUrl = 'ws://localhost:5001/connect/websocket';

  userServiceWebSocketUrl = 'wss://poker-user-service.herokuapp.com/connect/websocket';
  // userServiceWebSocketUrl = 'ws://localhost:5000/connect/websocket';
}
