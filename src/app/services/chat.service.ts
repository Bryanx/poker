import { Injectable } from '@angular/core';
import {WebsocketService} from './websocket.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  constructor(private websocketService: WebsocketService) { }

  send(url: string, inputMessage: string) {
    this.websocketService.getServer().send(url, {}, inputMessage);
  }
}
