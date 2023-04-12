import { AppComponent } from './../app.component';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { apiURL } from './apiURL';
import { Person } from '../person';
import { ResultComponent } from '../result/result.component';
import { PersonAdmin } from '../person-admin';
import { AdminService } from './admin.service';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  resultShowing: boolean = false;
  defaultSorting: Person[] = [];
  sortingValue: string = "rang";
  actualPage: number = 0;
  lastResults: Person[] = [];
  lastQuery: string = "";

  structureResult:string[]=[];

  constructor(private http: HttpClient) { }
  searchHouse(search:string)
  {

    this.http.get(apiURL + "/public/search/structure", { params: { name: search, page: this.actualPage, perPage: 15 } }).subscribe((data)=>{
      this.lastQuery = search;
      this.resultShowing = true;
      this.structureResult=data as [];

    });
  }
  search(search: string, isAdmin: any, filters:string,values:string) {

    if(isAdmin && filters!="" && values!="")
    {
      this.http.get(apiURL + "/admin/search/person", { params: { name: search,filter : filters, value: values,page :this.actualPage,perPage: 15 } }).subscribe((data)=>{
          this.lastQuery = search;
          this.resultShowing = true;
          this.lastResults = data as Person[];
          this.defaultSorting = this.lastResults.slice();
          this.sort(this.sortingValue);
          this.sortingValue = "rang";
      });
    }else {

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
    this.actualPage++;
    this.http.get(apiURL + "/public/search/person", { params: { name: this.lastQuery, page: this.actualPage, perPage: 15 } }).subscribe(
      (response) => {
        // console.log(response);
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
