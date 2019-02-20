import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthorizationService} from '../../services/authorization.service';
import {GameService} from '../../services/game.service';
import {switchMap} from 'rxjs/operators';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {Room} from '../../model/room';
import {User} from '../../model/user';

@Component({
  selector: 'app-game-room-admin',
  templateUrl: './game-room-admin.component.html',
  styleUrls: ['./game-room-admin.component.scss']
})
export class GameRoomAdminComponent implements OnInit {
  updateRoomForm: FormGroup;
  room: Room;

  constructor(
    private formBuilder: FormBuilder,
    private gameService: GameService,
    private curRouter: ActivatedRoute) {
  }

  ngOnInit() {
    this.curRouter.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.gameService.getRoom(+params.get('id'));
    })).subscribe((room) => {
      this.room = room as Room;
      this.updateRoomForm = this.formBuilder.group({
        name: [this.room.name, Validators.compose([Validators.required])],
        gameRule: [this.room.gameRules, Validators.compose([Validators.required])]
      })
    });
  }

  onSubmit() {
    this.room.name = this.updateRoomForm.controls.name.value;
    this.room.gameRules = this.updateRoomForm.controls.gameRules.value;
    this.gameService.changeRoom(this.room).subscribe(room => {
      // this.toastText = 'User was updated';
      // this.toast = true;
      // setTimeout(() => this.toast = false, 2000);
      console.log(room);
    }, error => {
      console.log(error.error.error_description);
    });
  }

}
