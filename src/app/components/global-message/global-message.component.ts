import { Component, OnInit } from '@angular/core';
import {Notification} from '../../model/notification';

@Component({
  selector: 'app-global-message',
  templateUrl: './global-message.component.html',
  styleUrls: ['./global-message.component.scss']
})
export class GlobalMessageComponent implements OnInit {
  notifications: Notification[] = [];
  inputString: String = '';

  constructor() { }

  ngOnInit() {
  }

  sendMessage() {
    // TODO
  }

  removeNotification(id: number) {
    // TODO
  }
}
