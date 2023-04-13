import { AfterViewInit, Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { elementAt, first } from 'rxjs';
import { Person } from '../person';
import { SearchService } from '../service/search.service';
import { LoginService } from '../service/login.service';
import { PersonAdmin } from '../person-admin';
@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent {
  listPerson: Person[] = [];
  clickedPosition: number = -1;
  personInfoClicked: PersonAdmin = { firstName: "", lastName: "", dn: "", title: "", login: "", email: "", personalPhone: "", professionalPhone: "", address: "", dateOfBirth: "", managerDN: "" };
  opacity: number = 0;
  i: number = 0;
  selectionState: string = "block";
  hideResponsiveDetails = false;
  form: FormControl = new FormControl("");
  constructor(public searchService: SearchService, public loginService: LoginService) {
    this.form.valueChanges.subscribe((e) => {
      this.searchService.sort(e);

    })
    //time out 1s to wait for the search service to be updated
    
  }




  displayNotFound(): string {

    if (this.searchService.lastResults.length === 0) {
      return "flex";
    }
    return "none";
  }

  personClicked(person: Person, position: number) {
    if (!this.loginService.getUser()?.admin) return;
    if (this.clickedPosition === position && !this.hideResponsiveDetails) {
      this.hideResponsiveDetails = true;
    }
    else {
      this.hideResponsiveDetails = false;
    }

    this.clickedPosition = position;
    this.opacity = 1;
    this.searchService.getInfos(person)?.subscribe((response: PersonAdmin) => {
      this.personInfoClicked = response;
    });

  }

  developDetails(i: number) {
    if (this.clickedPosition === i) {
      return 200;
    }
    return 0;
  }

}



