import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthorizationService} from '../../services/authorization.service';
import {UserService} from '../../services/user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpParams} from '@angular/common/http';
import {User} from '../../model/user';
import {combineLatest, concat} from 'rxjs';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  loginForm: FormGroup;
  returnUrl: string;
  error: string;
  user = new User();

  constructor(private formBuilder: FormBuilder, private authorizationService: AuthorizationService, private userService: UserService,
              private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    if (this.authorizationService.isAuthenticated()) {
      this.router.navigateByUrl('/');
    }

    this.loginForm = this.formBuilder.group({
      email: ['', Validators.compose([Validators.required])],
      username: ['', Validators.compose([Validators.required])],
      password: ['', Validators.required]
    });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }


  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    const body = new HttpParams()
      .set('username', this.loginForm.controls.username.value)
      .set('password', this.loginForm.controls.password.value)
      .set('grant_type', 'password');

    this.user.email = this.loginForm.controls.email.value;
    this.user.username = this.loginForm.controls.username.value;
    this.user.password = this.loginForm.controls.password.value;

    const auth$ = this.authorizationService.login(body.toString());
    const user$ = this.userService.addUser(this.user);
    concat(user$, auth$).subscribe(authResult => {
      this.authorizationService.setSession(authResult);
      this.router.navigateByUrl('/');
    }, error => {
      this.error = error.error.error_description;
    });
  }
}
