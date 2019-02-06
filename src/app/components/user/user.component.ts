import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthorizationService} from '../../services/authorization.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  updateUserForm: FormGroup;
  user: User;

  constructor(private formBuilder: FormBuilder, private authorizationService: AuthorizationService, private userService: UserService) { }

  ngOnInit() {
    this.userService.getUser().subscribe(result => {
      this.user = result as User;
      this.updateUserForm = this.formBuilder.group({
        username: [this.user.username, Validators.compose([Validators.required])]
      });
    }, error => {
      console.log(error.error.error_description);
    });
  }

  onSubmit() {
    this.user.username = this.updateUserForm.controls.username.value;
    this.userService.changeUser(this.user).subscribe(authResult => {
      this.authorizationService.setSession(authResult);
    }, error => {
      console.log(error.error.error_description);
    });
  }
}
