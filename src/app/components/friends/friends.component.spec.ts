import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendsComponent } from './friends.component';
import {SearchComponent} from '../search/search.component';
import {TranslatePipe} from '../../translate.pipe';
import {NgModel, ReactiveFormsModule} from '@angular/forms';
import {RouterLink, RouterModule} from '@angular/router';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';

describe('FriendsComponent', () => {
  let component: FriendsComponent;
  let fixture: ComponentFixture<FriendsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FriendsComponent, SearchComponent, TranslatePipe, NgModel ],
      imports: [ HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FriendsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
