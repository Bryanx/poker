import { Component, OnInit } from '@angular/core';
import {User} from '../../model/user';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthorizationService} from '../../services/authorization.service';
import {HttpParams} from '@angular/common/http';
import {UserService} from '../../services/user.service';
import {AuthService, FacebookLoginProvider} from 'angularx-social-login';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  returnUrl: string;
  error: string;
  user = new User();

  constructor(private formBuilder: FormBuilder, private authorizationService: AuthorizationService, private userService: UserService,
              private route: ActivatedRoute, private router: Router, private authService: AuthService) {
  }

  ngOnInit() {
    if (this.authorizationService.isAuthenticated()) {
      this.router.navigateByUrl('/');
    }

    this.loginForm = this.formBuilder.group({
      username: ['', Validators.compose([Validators.required])],
      password: ['', Validators.required]
    });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    this.authService.authState.subscribe((user) => {
      if (user != null) {
        this.user.id = user.id;
        this.user.email = user.email;
        this.user.username = user.name;
        this.user.firstname = user.firstName;
        this.user.lastname = user.lastName;
        this.user.profilePictureSocial = user.photoUrl;
        this.user.provider = user.provider;
        this.authorizationService.socialLogin(this.user).subscribe(authResult => {
          this.authorizationService.setSession(authResult);
          this.authService.signOut();
          this.router.navigateByUrl(this.returnUrl);
        });
      }
    });
  }


  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }
    const body = new HttpParams()
      .set('username', this.loginForm.controls.username.value)
      .set('password', this.loginForm.controls.password.value)
      .set('grant_type', 'password');

    this.authorizationService.login(body.toString()).subscribe(authResult => {
      this.authorizationService.setSession(authResult);
      this.router.navigateByUrl(this.returnUrl);
    }, error => {
      this.error = error.error.error_description;
    });
  }

  signInWithFB(): void {
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }
}
