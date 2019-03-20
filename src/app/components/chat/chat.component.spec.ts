import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {ChatComponent} from './chat.component';
import {CardComponent} from '../card/card.component';
import {NgModel, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {NotifierModule} from 'angular-notifier';
import {customNotifierOptions} from '../../notifierOptions';

/*
describe('CardComponent', () => {
let component: ChatComponent;
let fixture: ComponentFixture<ChatComponent>;


beforeEach(async(() => {
  TestBed.configureTestingModule({
    declarations: [ChatComponent, NgModel],
    imports: [HttpClientTestingModule, ReactiveFormsModule, NotifierModule.withConfig(customNotifierOptions)]
  })
    .compileComponents();
}));

beforeEach(() => {
  fixture = TestBed.createComponent(ChatComponent);
  component = fixture.componentInstance;
  fixture.detectChanges();
});

it('should create', () => {
  expect(component).toBeTruthy();
});


it('Should sent message in chat', () => {
  expect(component.messages.length).toBe(0);
  component.addMessage('Hello world!');
  expect(component.messages.length).toBe(1);
  expect(component.messages[0].content).toBe('Hello world!');
  expect(component.messages[0].name).toBe('system');
});

});

*/
