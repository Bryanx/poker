import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  serverUrl = 'ws://poker-game-service.herokuapp.com/chat/websocket';
  server;

  constructor() { }

  join() {
    this.server = Stomp.over(new WebSocket(this.serverUrl));
    return this.server;
  }

  send(url: string, inputMessage: string) {
    this.server.send(url, {}, inputMessage);
  }
}
