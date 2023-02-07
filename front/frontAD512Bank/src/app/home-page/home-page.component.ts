import { Component } from '@angular/core';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {

  constructor() {

   }

  openDropdown() {
    document.getElementsByClassName("dropdown")![0].classList.toggle("show");
  }
  selectHouse() {
    document.getElementById("house")!.style.display="block";
    document.getElementById("person")!.style.display="none";
    document.getElementById("house1")!.style.display="none";
    document.getElementById("person1")!.style.display="block";
  }
  selectPerson() {
    document.getElementById("person")!.style.display="block";
    document.getElementById("house")!.style.display="none";
    document.getElementById("person1")!.style.display="none";
    document.getElementById("house1")!.style.display="block";
  }

}
