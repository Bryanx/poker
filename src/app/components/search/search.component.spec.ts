import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SearchComponent} from './search.component';
import {FormsModule, NgModel, ReactiveFormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {TranslatePipe} from '../../translate.pipe';
import {RouterTestingModule} from '@angular/router/testing';

describe('SearchComponent', () => {
  let component: SearchComponent;
  let fixture: ComponentFixture<SearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SearchComponent, NgModel, TranslatePipe],
      imports: [HttpClientTestingModule, ReactiveFormsModule, RouterTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
