import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {GameService} from '../../services/game.service';
import {switchMap} from 'rxjs/operators';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {Room} from '../../model/room';
import {until} from 'selenium-webdriver';
import {Router} from '@angular/router';
import {GameRules} from '../../model/gamerules';

@Component({
  selector: 'app-game-room-admin',
  templateUrl: './game-room-admin.component.html',
  styleUrls: ['./game-room-admin.component.scss']
})
export class GameRoomAdminComponent implements OnInit {
  updateRoomForm: FormGroup;
  room: Room;
  id: number;
  maxPlayers: number[];

  constructor(
    private formBuilder: FormBuilder,
    private gameService: GameService,
    private curRouter: ActivatedRoute,
    private router: Router) {
    this.maxPlayers = new Array(5).fill(2).map((x, i) => i + 2);
  }

  ngOnInit() {
    if (this.isAdding()) {
      this.room = new Room();
      this.room.gameRules = new GameRules();
      this.room.playersInRoom = [];
      this.room.id = 0;
      this.room.name = "";
      this.room.gameRules.maxPlayerCount = 6;
      this.room.gameRules.smallBlind = 100;
      this.room.gameRules.playDelay = 30;
      this.room.gameRules.startingChips = 1000;
      this.updateRoomForm = this.formBuilder.group({
        name: [this.room.name, Validators.compose([Validators.required])],
        maxPlayerCount: [this.room.gameRules.maxPlayerCount, Validators.compose([Validators.required])],
        smallBlind: [this.room.gameRules.smallBlind, Validators.compose([Validators.required, Validators.min(10)])],
        playDelay: [this.room.gameRules.playDelay, Validators.compose([Validators.required, Validators.min(10)])],
        startingChips: [this.room.gameRules.startingChips, Validators.compose([Validators.required, Validators.min(500)])]
      })
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
        })
      });
    }
  }

  isAdding() {
    return this.router.url.split('/')[2] === 'add'
  }

  deleteRoom() {
    this.gameService.deleteRoom(this.room).subscribe(result => {
      return this.router.navigate(['/game-rooms'])
    });
  }

  onSubmit() {
    this.room.name = this.updateRoomForm.controls.name.value;
    this.room.gameRules.maxPlayerCount = this.updateRoomForm.controls.maxPlayerCount.value;
    this.room.gameRules.smallBlind = this.updateRoomForm.controls.smallBlind.value;
    this.room.gameRules.bigBlind = 2 * this.updateRoomForm.controls.smallBlind.value;
    this.room.gameRules.playDelay = this.updateRoomForm.controls.playDelay.value;
    this.room.gameRules.startingChips = this.updateRoomForm.controls.startingChips.value;
    if (this.isAdding()) {
      this.gameService.addRoom(this.room).subscribe(result => {
        return this.router.navigate(['/game-rooms'])
      });
    } else {
      this.gameService.changeRoom(this.room).subscribe(result => {
        return this.router.navigate(['/game-rooms'])
      });
    }
  }

  validationRequired(name: string) {
    return this.updateRoomForm.controls[name].hasError('required') && this.updateRoomForm.controls[name].touched;
  }

  validationMin(name: string) {
    return this.updateRoomForm.controls[name].hasError('min') && this.updateRoomForm.controls[name].touched;
  }

}
