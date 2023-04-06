/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { AdminService } from './admin.service';

import { HttpClientTestingModule } from '@angular/common/http/testing';
describe('Service: Admin', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AdminService]
    });
  });

  it('should ...', inject([AdminService], (service: AdminService) => {
    expect(service).toBeTruthy();
  }));
});
