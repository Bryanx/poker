import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import {UrlService} from './url.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly serverUrl: string;
  server;

  constructor(private urlService: UrlService) {
    this.serverUrl = urlService.serverUrl;
  }

  join() {
    this.server = Stomp.over(new WebSocket(this.serverUrl));
    this.server.heartbeat.outgoing = 10000; // send heartbeats every 10000ms
    this.server.heartbeat.incoming = 10000;
    this.server.debug = null;
    return this.server;
  }

  send(url: string, inputMessage: string) {
    this.server.send(url, {}, inputMessage);
  }
}
