import { Injectable } from '@angular/core';
import {UrlService} from './other/url.service';
import * as Stomp from 'stompjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private readonly userServiceWsUrl: string;
  private readonly gameServiceWsUrl: string;

  constructor(private urlService: UrlService) {
    this.userServiceWsUrl = this.urlService.userServiceWebSocketUrl;
    this.gameServiceWsUrl = this.urlService.gameServiceWebSocketUrl;
  }

  connectUserService() {
    const socket = new WebSocket(this.userServiceWsUrl);
    const ws = Stomp.over(socket);
    ws.debug = null;
    return ws;
  }

  connectGameService() {
    const socket = new WebSocket(this.gameServiceWsUrl);
    const ws = Stomp.over(socket);
    ws.debug = null;
    return ws;
  }
}
