import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { apiURL } from './apiURL';

@Injectable({
  providedIn: 'root'
})
export class AdminService {


  constructor(private http: HttpClient) { }

  public getMembers() {
    return this.http.get(apiURL + "/public/search/person", { params: { name: "a", page: 0, perPage: 30 } });
  }
  getGroups() {
    return this.http.get(apiURL + "/admin/group/all");
  }
}
