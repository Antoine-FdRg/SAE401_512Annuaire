import { AppComponent } from './../app.component';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { apiURL } from './apiURL';
import { Person } from '../person';
import { ResultComponent } from '../result/result.component';
import { PersonAdmin } from '../person-admin';
import { AdminService } from './admin.service';
import { Struct } from '../struct';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  resultShowing: boolean = false;
  personResults: boolean = false;
  structResults: boolean = false;
  defaultSorting: Person[] = [];
  sortingValue: string = "rang";
  actualPage: number = 0;
  lastResults: Person[] = [];
  lastQuery: string = "";
  morePageResult: boolean = true; // permet de savoir si le bouton "plus de résultats" doit être affiché
  structureResult: Struct[] = [];

  constructor(private http: HttpClient) { }
  searchStruct(search: string) {
    this.personResults = false;
    this.structResults = true;
    this.http.get(apiURL + "/public/search/structure", { params: { name: search, page: this.actualPage, perPage: 15 } }).subscribe((data) => {
      this.lastQuery = search;
      this.resultShowing = true;
      console.log(data);

      this.structureResult = data as Struct[];
      for (let i = 0; i < this.structureResult.length; i++) {
        let struct = this.structureResult[i];
        let value = struct.dn.substr(0, struct.dn.indexOf(","));
        value = value.replace("OU=", "");
        struct.title = value;
      }
    });
  }

  detailStruct(struct: Struct) {
    this.http.get(apiURL + "/admin/info/structure/" + encodeURIComponent(struct.dn)).subscribe((data) => {
      struct.members = (data as Struct).members;
    });
  }



  search(search: string, isAdmin: any, filters: string, values: string) {
    this.personResults = true;
    this.structResults = false;
    if (isAdmin && filters != "" && values != "") {
      this.http.get(apiURL + "/admin/search/person", { params: { name: search, filter: filters, value: values, page: this.actualPage, perPage: 15 } }).subscribe((data) => {
        this.lastQuery = search;
        this.resultShowing = true;
        this.lastResults = data as Person[];
        this.defaultSorting = this.lastResults.slice();
        this.sort(this.sortingValue);
        this.sortingValue = "rang";
      });
    } else {

      this.http.get(apiURL + "/public/search/person", { params: { name: search, page: this.actualPage, perPage: 15 } }).subscribe(
        (response) => {
          this.lastQuery = search;
          this.resultShowing = true;
          this.lastResults = response as Person[];
          this.defaultSorting = this.lastResults.slice();
          this.sort(this.sortingValue);
          this.sortingValue = "rang";
        }
      );
    }
  }

  getMorePage() {
    let somethingAdded: boolean = false;
    this.actualPage++;
    this.http.get<Person[]>(apiURL + "/public/search/person", { params: { name: this.lastQuery, page: this.actualPage, perPage: 15 } }).subscribe(
      (response) => {
        console.log(response);
        if (response.length != 0) {
          console.log("something added");

          somethingAdded = true;
        }
        this.morePageResult = somethingAdded;

        this.resultShowing = true;
        this.lastResults = this.lastResults.concat(response as Person[]);
        this.defaultSorting = this.lastResults.slice();
        this.sort(this.sortingValue)
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
    console.log(e);

    this.sortingValue = e;
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
    else if (e == "prenom") {

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
    else if (e == "rang") {
      this.lastResults = this.defaultSorting.slice();
      console.log(this.defaultSorting);

    }
  }
}
