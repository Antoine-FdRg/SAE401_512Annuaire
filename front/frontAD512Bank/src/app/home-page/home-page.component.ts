import { LoginService } from './../service/login.service';
import { AdminService } from 'src/app/service/admin.service';
import { SearchService } from './../service/search.service';
import { ApplicationRef, Component, ElementRef, Injector, ViewChild } from '@angular/core';
import { elementAt } from 'rxjs';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  public filterForm: FormControl = new FormControl("");
  public filterFormValue:FormControl= new FormControl("");

  private value:string="";
  private filter: string="";
  public branches = this.getFilters();
  private subject:string="person";
  constructor(private searchService: SearchService,private adminService: AdminService, public loginService: LoginService) {
    this.filterForm.valueChanges.subscribe((e) => {
      this.filter=e;
    })
    this.filterFormValue.valueChanges.subscribe((e) => {
      this.value=e;
    })
  }

  openDropdown() {
    document.getElementsByClassName("dropdown")![0].classList.toggle("show");
  }
  selectHouse() {
    this.subject="house";
    document.getElementById("house")!.style.display = "block";
    document.getElementById("person")!.style.display = "none";
    document.getElementById("house1")!.style.display = "none";
    document.getElementById("person1")!.style.display = "block";
  }
  selectPerson() {
    this.subject="person";
    document.getElementById("person")!.style.display = "block";
    document.getElementById("house")!.style.display = "none";
    document.getElementById("person1")!.style.display = "none";
    document.getElementById("house1")!.style.display = "block";
  }
  onSearch($event: SubmitEvent) {
    $event.preventDefault();
    var search = (<HTMLInputElement>document.getElementById("search")).value;

    if(this.subject==="house"){
      this.searchService.searchStruct(search);
    }
    else{
      this.searchService.search(search,this.loginService.getUser()?.admin,this.filter,this.value);
    }
  }

  showResults() {
    setTimeout(() => {
      window.scrollTo(0, 800);
    }
      , 300);
    this.searchService.resultShowing = true;
  }
  showResult() {
    return this.searchService.resultShowing;
  }
  getFilters(): any {
    this.adminService.getFilters().subscribe((data) => {
      this.branches = data;
    })
  }
}

