import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembersOfGroupComponent } from './members-of-group.component';

describe('MembersOfGroupComponent', () => {
  let component: MembersOfGroupComponent;
  let fixture: ComponentFixture<MembersOfGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MembersOfGroupComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MembersOfGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
