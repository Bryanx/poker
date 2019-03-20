import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthGuardService} from './services/security/auth-guard.service';
import {UserSettingsComponent} from './components/user-settings/user-settings.component';
import {LoginComponent} from './components/login/login.component';
import {HomeComponent} from './components/home/home.component';
import {RouterModule, Routes} from '@angular/router';
import {RegisterComponent} from './components/register/register.component';
import {GameRoomComponent} from './components/game-room/game-room.component';
import {ChatComponent} from './components/chat/chat.component';
import {RoomsOverviewComponent} from './components/rooms-overview/rooms-overview.component';
import {FriendsComponent} from './components/friends/friends.component';
import {UserAccountComponent} from './components/user-account/user-account.component';
import {SearchComponent} from './components/search/search.component';
import {GameRoomEditComponent} from './components/game-room-edit/game-room-edit.component';
import {RankingsComponent} from './components/rankings/rankings.component';
import {AdminGuardService} from './services/security/admin-guard.service';
import {GlobalMessageComponent} from './components/global-message/global-message.component';
import {ReplayComponent} from './components/replay/replay.component';


const routes: Routes = [
  {path: '', component: HomeComponent, pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'chat', component: ChatComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'friends', component: FriendsComponent, canActivate: [AuthGuardService]},
  {path: 'rankings', component: RankingsComponent, canActivate: [AuthGuardService]},
  {path: 'search', component: SearchComponent, canActivate: [AuthGuardService]},
  {path: 'replays', component: ReplayComponent, canActivate: [AuthGuardService]},
  {path: 'global-message', component: GlobalMessageComponent, canActivate: [AuthGuardService]},
  {path: 'rooms/private/add', component: GameRoomEditComponent, canActivate: [AuthGuardService]},
  {path: 'rooms/private/edit/:id', component: GameRoomEditComponent, canActivate: [AuthGuardService]},
  {path: 'rooms/private/settings', component: RoomsOverviewComponent, canActivate: [AuthGuardService]},
  {path: 'rooms/private', component: RoomsOverviewComponent, canActivate: [AuthGuardService]},
  {path: 'rooms/:id', component: GameRoomComponent, canActivate: [AuthGuardService]},
  {path: 'rooms', component: RoomsOverviewComponent, canActivate: [AuthGuardService]},
  {path: 'game-rooms', component: RoomsOverviewComponent, canActivate: [AdminGuardService]},
  {path: 'game-rooms/:id', component: GameRoomEditComponent, canActivate: [AdminGuardService]},
  {path: 'game-rooms/add', component: GameRoomEditComponent, canActivate: [AdminGuardService]},
  {path: 'settings', component: UserSettingsComponent, canActivate: [AuthGuardService]},
  {path: 'user/:id', component: UserAccountComponent, canActivate: [AuthGuardService]},
  { path: 'users', component: SearchComponent, canActivate: [AdminGuardService] },
  {path: '**', redirectTo: '/login', pathMatch: 'full'}
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes),
  ],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule {
}
