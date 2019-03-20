import {AfterViewChecked, Component, ElementRef, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ChatMessage} from '../../model/chat-message';
import {AuthorizationService} from '../../services/security/authorization.service';
import {WebSocketService} from '../../services/web-socket.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {
  error: Boolean = false;
  messages: ChatMessage[] = [];
  inputMessage: string;
  @Input() roomId: number;
  playerName: String;
  ws: any;
  @ViewChild('chatScroll') private chatScroll: ElementRef;
  systemScroll: boolean;
  chatVisible: boolean;

  constructor(private authorizationService: AuthorizationService, private websocketService: WebSocketService) {
  }

  ngOnInit() {
    this.playerName = this.authorizationService.getUsername();
    this.chatVisible = false;
    this.initializeChatConnection();
  }

  ngOnDestroy() {
    if (this.ws !== undefined) {
      this.ws.disconnect();
    }
  }

  ngAfterViewChecked() {
    if (this.systemScroll) {
      this.scrollToBottom();
    }
  }

  initializeChatConnection() {
    this.ws = this.websocketService.connectGameService();
    this.ws.connect({}, (frame) => {
      this.ws.subscribe('/chatroom/receive/' + this.roomId, (message) => {
        if (message) {
          this.messages.push(JSON.parse(message.body));
          this.systemScroll = true;
        }
      });
    });

    setTimeout(() => this.sendMessage('system', this.playerName + ' joined the room.'), 500);
  }

  sendMessage(name: String = this.playerName, messageString: String = this.inputMessage) {
    if (!messageString) {
      return;
    }
    this.inputMessage = '';
    const chatMessage = JSON.stringify({name: name, content: messageString});
    this.ws.send('/chatrooms/' + this.roomId + '/send', {}, chatMessage);
  }

  myMessage(message: ChatMessage) {
    return message.name === this.playerName;
  }

  systemMessage(message: ChatMessage) {
    return message.name === 'system';
  }

  addMessage(message: string) {
    const chatMessage = new ChatMessage();
    chatMessage.name = 'system';
    chatMessage.content = message;
    this.messages.push(chatMessage);
  }

  scrollToBottom(): void {
    this.chatScroll.nativeElement.scrollTop = this.chatScroll.nativeElement.scrollHeight;
  }

  onScroll() {
    if (this.systemScroll) {
      this.systemScroll = false;
    }
  }

  toggleVisible() {
    this.chatVisible = !this.chatVisible;
  }
}
