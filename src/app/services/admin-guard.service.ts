import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthorizationService} from './authorization.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuardService implements CanActivate{

  constructor(public authorizationService: AuthorizationService, public router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (!this.authorizationService.isAdmin()) {
      this.router.navigate(['login'], { queryParams: { returnUrl: state.url }});
      return false;
    }
    return true;
  }
}
