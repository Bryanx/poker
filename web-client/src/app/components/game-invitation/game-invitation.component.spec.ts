import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GameInvitationComponent} from './game-invitation.component';
import {TranslatePipe} from '../../translate.pipe';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

describe('GameInvitationComponent', () => {
  let component: GameInvitationComponent;
  let fixture: ComponentFixture<GameInvitationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GameInvitationComponent, TranslatePipe],
      imports: [HttpClientModule, BrowserAnimationsModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameInvitationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
