import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  serverUrl = 'wss://poker-game-service.herokuapp.com/chat/websocket';
  // serverUrl = 'ws://localhost:5001/chat/websocket';
  server;

  constructor() { }

  join() {
    this.server = Stomp.over(new WebSocket(this.serverUrl));
    this.server.heartbeat.outgoing = 10000; // send heartbeats every 10000ms
    this.server.heartbeat.incoming = 10000;
    return this.server;
  }

  send(url: string, inputMessage: string) {
    this.server.send(url, {}, inputMessage);
  }
}
