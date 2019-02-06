import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthorizationService} from "../../services/authorization.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  updateUserForm: FormGroup;
  returnUrl: string;

  user: User;

  constructor(private formBuilder: FormBuilder, private authorizationService: AuthorizationService, private userService: UserService,
              private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.userService.getUser().subscribe(result => {
      this.user = result as User;
      this.updateUserForm = this.formBuilder.group({
        username: [this.user.username, Validators.compose([Validators.required])]
      });
    });



    this.returnUrl = '/user';
  }

  private onSubmit() {
    console.log(this.user.username);
    this.user.username = this.updateUserForm.controls.username.value;
    this.userService.updateUser(this.user).subscribe(result => {
      this.user = result as User;
    });
  }

}
