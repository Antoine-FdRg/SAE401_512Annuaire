/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { MembersGestionComponent } from './membersGestion.component';

describe('MembersGestionComponent', () => {
  let component: MembersGestionComponent;
  let fixture: ComponentFixture<MembersGestionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MembersGestionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MembersGestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
