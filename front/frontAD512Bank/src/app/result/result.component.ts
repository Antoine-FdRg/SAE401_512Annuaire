import { Component } from '@angular/core';
import { elementAt, first } from 'rxjs';
import { Person } from '../person';
import { SearchService } from '../service/search.service';
@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent {
  listPerson: Person[] = [];
  clickedPosition: number = 0;
  opacity: number = 0;
  i: number = 0;
  selectionState : string = "block" 

  constructor(private searchService: SearchService) {
    this.listPerson = this.searchService.lastResults;
    this.listPerson.push(
      {
        firstName: "placeholder",
        lastName: "placeholder",
        phone: "placeholder",
        position: "placeholder",
        login: "placeholder",
        tutor: "placeholder",
        email: "placeholder"
      },
      {
        firstName: "placeholder",
        lastName: "placeholder",
        phone: "placeholder",
        position: "placeholder",
        login: "placeholder",
        tutor: "placeholder",
        email: "placeholder"
      },
      {
        firstName: "placeholder",
        lastName: "placeholder",
        phone: "placeholder",
        position: "placeholder",
        login: "placeholder",
        tutor: "placeholder",
        email: "placeholder"
      },
    )
    this.listPerson = [];
    // scroll to top of component
  }
  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
    window.scrollTo(0, 1000);
  }

  displayNotFound() : string {
    if(this.listPerson.length === 0){
      return "flex";
    }
    return "none";
  }



  personClicked(person: Person, position: number) {
    this.clickedPosition = position;
    this.opacity = 1;
  
  }
}



