import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const tokenUrl = '/oauth/token';
    const idToken = localStorage.getItem('jwt_token');

    if (req.url.search(tokenUrl) !== -1 || !idToken) {
      return next.handle(req);
    }
    const cloned = req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + idToken)
    });
    return next.handle(cloned);
  }
}
