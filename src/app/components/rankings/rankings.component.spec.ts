import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RankingsComponent } from './rankings.component';
import {MatTableModule} from '@angular/material';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {TranslatePipe} from '../../translate.pipe';

describe('RankingsComponent', () => {
  let component: RankingsComponent;
  let fixture: ComponentFixture<RankingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RankingsComponent, TranslatePipe],
      imports: [MatTableModule, RouterTestingModule, HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RankingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
