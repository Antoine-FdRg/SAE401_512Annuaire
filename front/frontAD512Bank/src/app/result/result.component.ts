import { Component } from '@angular/core';
import { Person } from '../person';
import { SearchService } from '../service/search.service';
@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent {
  listPerson: Person[] = [];

  constructor(private searchService: SearchService) {
    this.listPerson = this.searchService.lastResults;
    console.log(this.listPerson);
    // scroll to top of component

  }
  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
    window.scrollTo(0, 1000);
  }

  addPerson(person: Person) {
    this.listPerson.push(person);
  }
}
