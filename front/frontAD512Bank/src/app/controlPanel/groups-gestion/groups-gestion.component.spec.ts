import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupsGestionComponent } from './groups-gestion.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
describe('GroupsGestionComponent', () => {
  let component: GroupsGestionComponent;
  let fixture: ComponentFixture<GroupsGestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GroupsGestionComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GroupsGestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
