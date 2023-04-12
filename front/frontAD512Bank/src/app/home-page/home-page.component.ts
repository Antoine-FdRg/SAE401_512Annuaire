import { LoginService } from './../service/login.service';
import { AdminService } from 'src/app/service/admin.service';
import { SearchService } from './../service/search.service';
import { ApplicationRef, Component, ElementRef, Injector, ViewChild } from '@angular/core';
import { elementAt } from 'rxjs';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {

  public branches = this.getFilters();
  constructor(private searchService: SearchService,private adminService: AdminService, public loginService: LoginService) {

  }

  openDropdown() {
    document.getElementsByClassName("dropdown")![0].classList.toggle("show");
  }
  selectHouse() {
    document.getElementById("house")!.style.display = "block";
    document.getElementById("person")!.style.display = "none";
    document.getElementById("house1")!.style.display = "none";
    document.getElementById("person1")!.style.display = "block";
  }
  selectPerson() {
    document.getElementById("person")!.style.display = "block";
    document.getElementById("house")!.style.display = "none";
    document.getElementById("person1")!.style.display = "none";
    document.getElementById("house1")!.style.display = "block";
  }
  onSearch($event: SubmitEvent) {
    $event.preventDefault();
    var search = (<HTMLInputElement>document.getElementById("search")).value;
    this.searchService.search(search);
  }

  showResults() {
    this.searchService.resultShowing = true;
  }
  getFilters() : any
  {
    this.adminService.getFilters().subscribe((data)=>{
      console.log(data);

      this.branches=data;

    })
  }
}

