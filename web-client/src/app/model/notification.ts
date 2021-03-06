import {NotificationType} from './notificationType';
import {User} from './user';
import {Time} from '@angular/common';

export class Notification {
  id: number;
  message: string;
  type: NotificationType;
  read: boolean;
  timestamp: string;
  ref: string;
}
