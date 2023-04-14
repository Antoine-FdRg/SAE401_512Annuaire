import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMemberToGroupComponent } from './add-member-to-group.component';

describe('AddMemberToGroupComponent', () => {
  let component: AddMemberToGroupComponent;
  let fixture: ComponentFixture<AddMemberToGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddMemberToGroupComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddMemberToGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
