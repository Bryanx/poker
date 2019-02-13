import { BrowserModule } from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {MatSliderModule} from '@angular/material/slider';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthInterceptor} from './interceptors/authInterceptor';
import { UserSettingsComponent } from './components/user-settings/user-settings.component';
import { HomeComponent } from './components/home/home.component';
import { AppRoutingModule } from './app-routing.module';
import {RouterModule} from '@angular/router';
import { RegisterComponent } from './components/register/register.component';
import { GameRoomComponent } from './components/game-room/game-room.component';
import { PlayerComponent } from './components/player/player.component';
import { GameTableComponent } from './components/game-table/game-table.component';
import { CardComponent } from './components/card/card.component';
import { ActionbarComponent } from './components/actionbar/actionbar.component';
import { ChatComponent } from './components/chat/chat.component';
import { RoomsOverviewComponent } from './components/rooms-overview/rooms-overview.component';
import {AuthServiceConfig, FacebookLoginProvider, SocialLoginModule} from 'angularx-social-login';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TranslateService} from './services/translate.service';
import { TranslatePipe } from './translate.pipe';

const config = new AuthServiceConfig([
  {
    id: FacebookLoginProvider.PROVIDER_ID,
    provider: new FacebookLoginProvider('483584635507231')
  }
]);

export function provideConfig() {
  return config;
}

export function setupTranslateFactory(
  service: TranslateService): Function {
  return () => {
    const lang = localStorage.getItem('lang');
    if (lang === null) {
      service.use('en');
    } else {
      service.use(lang);
    }
  };
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UserSettingsComponent,
    HomeComponent,
    RegisterComponent,
    GameRoomComponent,
    PlayerComponent,
    GameTableComponent,
    CardComponent,
    ActionbarComponent,
    ChatComponent,
    RoomsOverviewComponent,
    TranslatePipe,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatSliderModule,
    FormsModule,
    RouterModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    SocialLoginModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: AuthServiceConfig,
      useFactory: provideConfig
    },
    TranslateService,
    {
      provide: APP_INITIALIZER,
      useFactory: setupTranslateFactory,
      deps: [ TranslateService ],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
