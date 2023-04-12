import { AppComponent } from './../app.component';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { apiURL } from './apiURL';
import { Person } from '../person';
import { ResultComponent } from '../result/result.component';

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
    this.http.get(apiURL + "/public/info/person/" + person.dn).subscribe(
      (response) => {
        console.log(response);
      }
    );
  }

  sort(e:string)
  {
    if(e=="nom")
      {
        this.lastResults.sort((a:Person,b:Person)=>{
          if (a.lastName < b.lastName) {
            return -1;
          }
          if (a.lastName > b.lastName) {
            return 1;
          }
          return 0;
        });
      }
      if(e=="prenom")
      {

        this.lastResults.sort((a:Person,b:Person)=>{
          if (a.firstName < b.firstName) {
            return -1;
          }
          if (a.firstName > b.firstName) {
            return 1;
          }
          return 0;
        });
      }
      if(e=="rang")
      {




      }
  }
}
