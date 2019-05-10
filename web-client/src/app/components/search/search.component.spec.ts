import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SearchComponent} from './search.component';
import {NgModel, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {TranslatePipe} from '../../translate.pipe';
import {RouterTestingModule} from '@angular/router/testing';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';
import {User} from '../../model/user';
import {By} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

describe('SearchComponent', () => {
  let component: SearchComponent;
  let fixture: ComponentFixture<SearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SearchComponent, NgModel, TranslatePipe],
      imports: [HttpClientTestingModule, ReactiveFormsModule, RouterTestingModule, BrowserAnimationsModule,
        NotifierModule.withConfig(customNotifierOptions)]
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

  it('Should display searched users', () => {
    const user: User = new User();
    user.username = 'joske vermeiren';
    component.users.push(user);
    component.typed = true;
    fixture.detectChanges();

    const userSpanTags = fixture.debugElement.queryAll(By.css('.username-span'));
    expect(userSpanTags[0].nativeElement.innerHTML).toContain('joske vermeiren');
  });
});
