import {Component, Input, OnInit} from '@angular/core';
import {ChatService} from '../../services/chat.service';
import {Message} from '../../model/message';
import {AuthorizationService} from '../../services/authorization.service';
import {WebsocketService} from '../../services/websocket.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  error: Boolean = false;
  messages: Message[] = [];
  inputMessage: string;
  @Input() roomId: number;
  playerName: String;

  constructor(private chatService: ChatService, private authorizationService: AuthorizationService,
              private websocketService: WebsocketService) {
  }

  ngOnInit() {
    this.playerName = this.authorizationService.getUsername();
    this.initializeChatConnection();
  }

  initializeChatConnection() {
    const server = this.websocketService.join();
    server.connect({}, () => {
      server.subscribe('/chatroom/receive/' + this.roomId, message => {
        message = JSON.parse(message.body);
        if (message) {
          this.messages.push(message);
        }
      }, e => {
        this.error = true;
      });
      // TODO: Send message from the server when the player actually joins.
      this.sendMessage('system', this.playerName + ' joined the room.');
    }, e => {
      this.error = true;
    });
  }

  sendMessage(name: String = this.playerName, messageString: String = this.inputMessage) {
    if (!messageString) {
      return;
    }
    this.inputMessage = '';
    const message = JSON.stringify({name: name, content: messageString});
    this.chatService.send('/chatroom/send/' + this.roomId, message);
  }

  myMessage(message: Message) {
    return message.name === this.playerName;
  }

  systemMessage(message: Message) {
    return message.name === 'system';
  }
}
