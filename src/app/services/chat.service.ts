import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  serverUrl = 'https://poker-game-service.herokuapp.com/chat';
  server;

  constructor() { }

  join() {
    this.server = Stomp.over(new SockJS(this.serverUrl));
    return this.server;
  }

  send(url: string, inputMessage: string) {
    this.server.send(url, {}, inputMessage);
  }
}
