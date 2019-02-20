import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ChatMessage} from '../../model/chat-message';
import {AuthorizationService} from '../../services/authorization.service';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnDestroy {
  error: Boolean = false;
  messages: ChatMessage[] = [];
  inputMessage: string;
  @Input() roomId: number;
  playerName: String;
  chatSubscription: Subscription;

  constructor(private authorizationService: AuthorizationService, private websocketService: RxStompService) {}

  ngOnInit() {
    this.playerName = this.authorizationService.getUsername();
    this.initializeChatConnection();
  }

  ngOnDestroy() {
    this.chatSubscription.unsubscribe();
  }

  initializeChatConnection() {
    this.chatSubscription = this.websocketService.watch('/chatroom/receive/' + this.roomId).subscribe((message: Message) => {
      if (message) {
        this.messages.push(JSON.parse(message.body));
      }
    }, error => {
      this.error = true;
    });
    setTimeout(() => this.sendMessage('system', this.playerName + ' joined the room.'), 500);
  }

  sendMessage(name: String = this.playerName, messageString: String = this.inputMessage) {
    if (!messageString) {
      return;
    }
    this.inputMessage = '';
    const chatMessage = JSON.stringify({name: name, content: messageString});
    this.websocketService.publish({destination: '/chatroom/send/' + this.roomId, body: chatMessage});
  }

  myMessage(message: ChatMessage) {
    return message.name === this.playerName;
  }

  systemMessage(message: ChatMessage) {
    return message.name === 'system';
  }
}
