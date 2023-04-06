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
  lastResults: Person[] = [];
  constructor(private http: HttpClient) { }

  search(search: string) {
    this.http.get(apiURL + "/public/search/person", { params: { name: search } }).subscribe(
      (response) => {
        // console.log(response);
        this.resultShowing = true;
        this.lastResults = response as Person[];
        console.log(this.lastResults);
      }
    );


  }

  getInfos(person: Person) {
    console.log(person);

    this.http.get(apiURL + "/admin/info/person?cn=" + person.cn).subscribe(
      (response) => {
        console.log(response);
      }
    );
  }


}
