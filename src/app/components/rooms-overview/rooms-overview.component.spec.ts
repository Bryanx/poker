import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RoomsOverviewComponent} from './rooms-overview.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {TranslatePipe} from '../../translate.pipe';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';

describe('RoomsOverviewComponent', () => {
  let component: RoomsOverviewComponent;
  let fixture: ComponentFixture<RoomsOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RoomsOverviewComponent, TranslatePipe],
      imports: [RouterTestingModule, HttpClientTestingModule, NotifierModule.withConfig(customNotifierOptions)]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
