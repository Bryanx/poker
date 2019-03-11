import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UrlService} from './url.service';
import {Observable} from 'rxjs';
import {User} from '../model/user';
import {Notification} from '../model/notification';
import {Auth} from '../model/auth';


/**
 * This service is used to make API calls to the user micro service backend.
 */
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly url: string;

  constructor(private http: HttpClient, private urlService: UrlService) {
    this.url = urlService.userServiceUrl;
  }

  /**
   * Gives back your own credentials.
   */
  getMyself(): Observable<User> {
    return this.http.get<User>(this.url);
  }

  /**
   * Returns the requested user.
   *
   * @param userId The id of the requested user.
   */
  getUser(userId: string): Observable<User> {
    return this.http.get<User>(this.url + '/' + userId);
  }

  /**
   * Returns all the users that are in the system.
   */
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.url + 's');
  }

  /**
   * Returns all the users that are in the system.
   */
  getAdmins(): Observable<User[]> {
    return this.http.get<User[]>(this.url + 's/admin/all');
  }

  /**
   * Gives back all the users that have the name-string inside
   * their name.
   *
   * @param name The name of the users.
   */
  getUsersByName(name: string): Observable<User[]> {
    return this.http.get<User[]>(this.url + 's/' + name);
  }

  /**
   * Adds a user to the system. Usually called on when a users registers.
   *
   * @param user The newly registered user.
   */
  addUser(user: User): Observable<Auth> {
    return this.http.post<Auth>(this.url, user);
  }

  getAdminNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.url + '/notifications/admin');
  }

  /**
   * Sent a notification to a specific user
   *
   * @param receiverId The id of the person that needs to receive it.
   * @param not The notification that is coupled to the receiver.
   */
  sendNotification(receiverId: string, not: Notification): Observable<Notification> {
    return this.http.post<Notification>(this.url + '/' + receiverId + '/send-notification', not);
  }

  sendPublicNotification(not: Notification): Observable<Notification> {
    return this.http.post<Notification>(this.url + '/notifications/public', not);
  }

  /**
   * Sets a specific notification to read.
   *
   * @param notId The if of the notification that needs be set to read.
   */
  readNotification(notId: number): Observable<Notification> {
    return this.http.patch<Notification>(this.url + '/notifications/' + notId + '/read-notification', '');
  }

  /**
   * Gives back all the unread _notifications of a specific user.3
   */
  getUnReadNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.url + '/notifications/un-read');
  }

  /**
   * Gives back all the unread _notifications of a specific user.3
   */
  getNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.url + '/notifications');
  }

  /**
   * Changes one or more attributes of the user except the password.
   *
   * @param user The user that needs to be changed.
   */
  changeUser(user: User): Observable<Auth> {
    return this.http.put<Auth>(this.url, user);
  }

  addXp(xp: number): Observable<User> {
    return this.http.patch<User>(this.url + '/level/' + xp, '');
  }

  /**
   * Changes the password of the given user.
   *
   * @param user The user with the new password.
   */
  changePassword(user: any): Observable<Auth> {
    return this.http.patch<any>(this.url, user);
  }

  disableUser(user: User) {
    return this.http.put(this.url + '/disable', user)
  }

  changeToAdmin(user: User) {
    return this.http.put(this.url + '/' + user.id + '/admin', user)
  }

  changeToUser(user: User) {
    return this.http.put(this.url + '/' + user.id + '/user', user)
  }

  deleteNotification(id: number): Observable<Notification> {
    return this.http.delete<Notification>(this.url + '/notification/' + id);
  }

  deleteNotifications(): Observable<Notification> {
    return this.http.delete<Notification>(this.url + '/notification');
  }
}
