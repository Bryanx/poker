import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthorizationService} from '../../services/authorization.service';
import { Location } from '@angular/common';
import {DomSanitizer} from '@angular/platform-browser';
import {TranslateService} from '../../services/translate.service';

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
    private location: Location,
    private sanitizer: DomSanitizer,
    private translate: TranslateService) {}

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

  getProfilePicture() {
    if (this.user.profilePicture === null) {
      return this.user.profilePictureSocial;
    } else {
      return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.user.profilePicture);
    }
  }

  changeProfilePicture(event) {
    const file = event.target.files[0];
    console.log(this.user.profilePicture);
    const reader = new FileReader();
    reader.onload = this.handleReaderLoaded.bind(this);
    reader.readAsBinaryString(file);
  }

  handleReaderLoaded(readerEvt) {
    const binaryString = readerEvt.target.result;
    this.user.profilePicture = btoa(binaryString);
    console.log(btoa(binaryString));
    this.onSubmit();
  }
}
