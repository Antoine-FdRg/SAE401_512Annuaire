import { AppComponent } from './../app.component';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { apiURL } from './apiURL';
import { Person } from '../person';
import { ResultComponent } from '../result/result.component';
import { PersonAdmin } from '../person-admin';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  resultShowing: boolean = false;
  actualPage: number = 0;
  lastResults: Person[] = [];
  constructor(private http: HttpClient) { }

  search(search: string) {
    this.http.get(apiURL + "/public/search/person", { params: { name: search, page: this.actualPage, perPage: 15 } }).subscribe(
      (response) => {
        this.resultShowing = true;
        this.lastResults = response as Person[];
      }
    );
  }
  getMorePage() {
    this.actualPage++;
    this.http.get(apiURL + "/public/search/person", { params: { name: "", page: this.actualPage, perPage: 15 } }).subscribe(
      (response) => {
        // console.log(response);
        this.resultShowing = true;
        this.lastResults = this.lastResults.concat(response as Person[]);
        console.log(this.lastResults);
      }
    );
  }

  getInfos(person: Person) {
    if (!person) return;
    if (!person.dn) return;
    let dn: string = person.dn;

    return this.http.get<PersonAdmin>(apiURL + "/admin/info/person", { params: { dn: dn } });
  }

  sort(e: string) {
    if (e == "nom") {
      this.lastResults.sort((a: Person, b: Person) => {
        if (a.lastName < b.lastName) {
          return -1;
        }
        if (a.lastName > b.lastName) {
          return 1;
        }
        return 0;
      });
    }
    if (e == "prenom") {

      this.lastResults.sort((a: Person, b: Person) => {
        if (a.firstName < b.firstName) {
          return -1;
        }
        if (a.firstName > b.firstName) {
          return 1;
        }
        return 0;
      });
    }
    if (e == "rang") {




    }
  }
}
