import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultComponent } from './result.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
describe('ResultComponent', () => {
  let component: ResultComponent;
  let fixture: ComponentFixture<ResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],

      declarations: [ResultComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
