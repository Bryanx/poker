import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {GameService} from '../../services/game.service';
import {switchMap} from 'rxjs/operators';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {Room} from '../../model/room';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {PrivateRoom} from '../../model/privateRoom';

@Component({
  selector: 'app-game-room-admin',
  templateUrl: './game-room-edit.component.html',
  styleUrls: ['./game-room-edit.component.scss']
})
export class GameRoomEditComponent implements OnInit {
  updateRoomForm: FormGroup;
  room: Room = Room.create();
  id: number;
  maxPlayers: number[];

  constructor(
    private formBuilder: FormBuilder,
    private gameService: GameService,
    private curRouter: ActivatedRoute,
    private location: Location,
    private router: Router) {
    this.maxPlayers = new Array(5).fill(2).map((x, i) => i + 2);
  }

  ngOnInit() {
    if (this.isAdding()) {
      this.updateRoomForm = this.formBuilder.group({
        name: [this.room.name, Validators.compose([Validators.required])],
        maxPlayerCount: [this.room.gameRules.maxPlayerCount, Validators.compose([Validators.required])],
        smallBlind: [this.room.gameRules.smallBlind, Validators.compose([Validators.required, Validators.min(10)])],
        playDelay: [this.room.gameRules.playDelay, Validators.compose([Validators.required, Validators.min(10)])],
        startingChips: [this.room.gameRules.startingChips, Validators.compose([Validators.required, Validators.min(500)])]
      });
    } else {
      this.curRouter.paramMap.pipe(switchMap((params: ParamMap) => {
        this.id = +params.get('id');
        return this.gameService.getRoom(+params.get('id'));
      })).subscribe((room) => {
        this.room = room as Room;
        this.room.id = this.id;
        this.updateRoomForm = this.formBuilder.group({
          name: [this.room.name, Validators.compose([Validators.required])],
          maxPlayerCount: [this.room.gameRules.maxPlayerCount, Validators.compose([Validators.required])],
          smallBlind: [this.room.gameRules.smallBlind, Validators.compose([Validators.required, Validators.min(10)])],
          playDelay: [this.room.gameRules.playDelay, Validators.compose([Validators.required, Validators.min(10)])],
          startingChips: [this.room.gameRules.startingChips, Validators.compose([Validators.required, Validators.min(500)])]
        });
      });
    }
  }

  isAdding() {
    return this.router.url.includes('add');
  }

  isAddingPrivate() {
    return this.router.url.includes('add')
      && this.router.url.includes('private');
  }

  deleteRoom() {
    this.gameService.deleteRoom(this.room).subscribe(() => this.location.back());
  }

  onSubmit() {
    this.room.name = this.updateRoomForm.controls.name.value;
    this.room.gameRules.maxPlayerCount = this.updateRoomForm.controls.maxPlayerCount.value;
    this.room.gameRules.smallBlind = this.updateRoomForm.controls.smallBlind.value;
    this.room.gameRules.bigBlind = 2 * this.updateRoomForm.controls.smallBlind.value;
    this.room.gameRules.playDelay = this.updateRoomForm.controls.playDelay.value;
    this.room.gameRules.startingChips = this.updateRoomForm.controls.startingChips.value;

    if (this.isAddingPrivate()) {
      this.gameService.addPrivateRoom(this.room as PrivateRoom).subscribe(() => this.location.back());
    } else if (this.isAdding()) {
      this.gameService.addRoom(this.room).subscribe(() => this.location.back());
    } else {
      this.gameService.changeRoom(this.room).subscribe(() => this.location.back());
    }
  }

  validationRequired(name: string) {
    return this.updateRoomForm.controls[name].hasError('required') && this.updateRoomForm.controls[name].touched;
  }

  validationMin(name: string) {
    return this.updateRoomForm.controls[name].hasError('min') && this.updateRoomForm.controls[name].touched;
  }

  goBackPrev() {
    this.location.back();
  }
}
