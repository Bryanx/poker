import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  // serverUrl = 'wss://poker-game-service.herokuapp.com/chat/websocket';
  serverUrl = 'ws://localhost:5001/connect/websocket';
  server;

  constructor() {
  }

  join() {
    this.server = Stomp.over(new WebSocket(this.serverUrl));
    this.server.heartbeat.outgoing = 10000; // send heartbeats every 10000ms
    this.server.heartbeat.incoming = 10000;
    this.server.debug = null;
    return this.server;
  }

  getServer() {
    return this.server;
  }
}
