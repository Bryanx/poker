import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthorizationService} from '../../services/authorization.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-user',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss']
})
export class UserSettingsComponent implements OnInit {
  updateUserForm: FormGroup;
  updatePasswordForm: FormGroup;
  user: User;
  toast: boolean;
  toastText: string;

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
    }, error => {
      console.log(error.error.error_description);
    });

    this.updatePasswordForm = this.formBuilder.group({
      password: ['', Validators.compose([Validators.required])]
    });
  }

  onSubmit() {
    this.user.username = this.updateUserForm.controls.username.value;
    this.user.firstname = this.updateUserForm.controls.firstname.value;
    this.user.lastname = this.updateUserForm.controls.lastname.value;
    this.user.email = this.updateUserForm.controls.email.value;
    this.userService.changeUser(this.user).subscribe(authResult => {
      this.authorizationService.setSession(authResult);
      this.toastText = 'User was updated';
      this.toast = true;
      setTimeout(() => this.toast = false, 2000);
    }, error => {
      console.log(error.error.error_description);
    });
  }

  onSubmitPasswordChange() {
    this.user.password = this.updatePasswordForm.controls.password.value;
    this.userService.changePassword(this.user).subscribe(authResult => {
      this.toastText = 'User password was updated';
      this.toast = true;
      setTimeout(() => this.toast = false, 2000);
    }, error => {
      console.log(error.error.error_description);
    });
  }

  goBack() {
    this.location.back();
  }
}
