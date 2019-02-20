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
  roomId: number;

  constructor(
    private formBuilder: FormBuilder,
    private gameService: GameService,
    private curRouter: ActivatedRoute) {
  }

  ngOnInit() {
    this.curRouter.paramMap.pipe(switchMap((params: ParamMap) => {
      this.roomId = +params.get('id');
      return this.gameService.getRoom(+params.get('id'));
    })).subscribe((room) => {
      this.room = room as Room;
      this.room.roomId = this.roomId;
      this.updateRoomForm = this.formBuilder.group({
        name: [this.room.name, Validators.compose([Validators.required])],
        maxPlayerCount: [this.room.gameRules.maxPlayerCount, Validators.compose([Validators.required])],
        smallBlind: [this.room.gameRules.smallBlind, Validators.compose([Validators.required])],
        playDelay: [this.room.gameRules.playDelay, Validators.compose([Validators.required])],
        startingChips: [this.room.gameRules.startingChips, Validators.compose([Validators.required])],
      })
    });
  }

  onSubmit() {
    this.room.name = this.updateRoomForm.controls.name.value;
    this.room.gameRules.maxPlayerCount = this.updateRoomForm.controls.maxPlayerCount.value;
    this.room.gameRules.smallBlind = this.updateRoomForm.controls.smallBlind.value;
    this.room.gameRules.playDelay = this.updateRoomForm.controls.playDelay.value;
    this.room.gameRules.startingChips = this.updateRoomForm.controls.startingChips.value;
    this.gameService.changeRoom(this.room).subscribe(room => {
      // this.toastText = 'User was updated';
      // this.toast = true;
      // setTimeout(() => this.toast = false, 2000);
    });
  }

}
