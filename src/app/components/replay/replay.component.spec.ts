import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ReplayComponent} from './replay.component';
import {TranslatePipe} from '../../translate.pipe';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ReplayLine} from '../../model/replayLine';
import {Replay} from '../../model/replay';
import {By} from '@angular/platform-browser';

describe('ReplayComponent', () => {
  let component: ReplayComponent;
  let fixture: ComponentFixture<ReplayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ReplayComponent, TranslatePipe],
      imports: [HttpClientTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should switch the pages correctly', () => {
    component.replays.push(null);
    component.replays.push(null);
    component.replays.push(null);

    component.switchPage(1);
    component.switchPage(1);
    expect(component.curReplay).toBe(2);
    component.switchPage(1);
    component.switchPage(1);
    expect(component.curReplay).toBe(0);
    component.switchPage(-1);
    expect(component.curReplay).toBe(3);
  });

  it('Should display replay', () => {
    const line: ReplayLine = new ReplayLine();
    line.phase = 'TURN';
    line.line = 'a message';
    const replay: Replay = new Replay();
    replay.roundNumber = 12345;
    replay.roomName = 'test';
    replay.lines = [line];
    component.replays.pop(); // pop default replay
    component.replays.push(replay);
    component.curReplay = 0;
    fixture.detectChanges();

    const roomNameAndRoundNumberTag = fixture.debugElement.query(By.css('h2'));
    expect(roomNameAndRoundNumberTag.nativeElement.innerHTML).toContain('test');
    expect(roomNameAndRoundNumberTag.nativeElement.innerHTML).toContain('12345');

    const lineTags = fixture.debugElement.queryAll(By.css('.message-row'));
    const phaseTags = fixture.debugElement.queryAll(By.css('.phase'));
    expect(lineTags[0].nativeElement.innerHTML).toContain('a message');
    expect(phaseTags[0].nativeElement.innerHTML).toContain('TURN');
  });
});
