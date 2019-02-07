import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthorizationService} from '../../services/authorization.service';
import { Location } from '@angular/common';
import {HttpParams} from "@angular/common/http";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  updateUserForm: FormGroup;
  updatePasswordForm: FormGroup;
  user: User;

  constructor(
    private formBuilder: FormBuilder,
    private authorizationService: AuthorizationService,
    private userService: UserService,
    private location: Location
  ) { }

  ngOnInit() {
    this.userService.getUser().subscribe(result => {
      this.user = result as User;
      this.updateUserForm = this.formBuilder.group({
        username: [this.user.username, Validators.compose([Validators.required])],
        firstname: [this.user.firstname, Validators.compose([Validators.required])],
        lastname: [this.user.lastname, Validators.compose([Validators.required])],
        email: [this.user.email, Validators.compose([Validators.required, Validators.email])]
      });
      this.updatePasswordForm = this.formBuilder.group({
        password: ['', Validators.compose([Validators.required])],
        controlPassword: ['', Validators.compose([Validators.required])]
      });
    }, error => {
      console.log(error.error.error_description);
    });
  }

  private onSubmit() {
    console.log('Update User');
    this.user.username = this.updateUserForm.controls.username.value;
    this.user.firstname = this.updateUserForm.controls.firstname.value;
    this.user.lastname = this.updateUserForm.controls.lastname.value;
    this.user.email = this.updateUserForm.controls.email.value;
    this.userService.changeUser(this.user).subscribe(authResult => {
      this.authorizationService.setSession(authResult);
    }, error => {
      console.log(error.error.error_description);
    });
  }

  private onSubmitPasswordChange() {
    console.log('Update password');
    this.user.password = this.updatePasswordForm.controls.password.value;

    const body = new HttpParams()
      .set('username', this.user.username)
      .set('password', this.updatePasswordForm.controls.password.value);

    this.userService.updatePassword(body.toString()).subscribe(authResult => {
      this.authorizationService.setSession(authResult);
    }, error => {
      console.log(error.error.error_description);
    });
  }

  private goBack() {
    this.location.back();
  }
}
