import { AfterViewInit, Component } from '@angular/core';
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
  selectionState: string = "block"

  constructor(public searchService: SearchService) {
  }


  ngAfterContentChecked(): void {
    //Called after every check of the component's or directive's content.
    //Add 'implements AfterContentChecked' to the class.
    window.scrollTo(0, 800);
  }

  displayNotFound(): string {
    if (this.searchService.lastResults.length === 0) {
      return "flex";
    }
    return "none";
  }

  personClicked(person: Person, position: number) {
    this.clickedPosition = position;
    this.opacity = 1;
  }

  developDetails(i: number) {
    if (this.clickedPosition === i) {
      return 200;
    }
    return 0;
  }


}



