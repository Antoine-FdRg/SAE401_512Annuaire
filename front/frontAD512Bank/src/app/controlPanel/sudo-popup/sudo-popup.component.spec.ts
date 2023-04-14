import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SudoPopupComponent } from './sudo-popup.component';

describe('SudoPopupComponent', () => {
  let component: SudoPopupComponent;
  let fixture: ComponentFixture<SudoPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SudoPopupComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SudoPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
