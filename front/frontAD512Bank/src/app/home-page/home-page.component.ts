import { Component, ElementRef, ViewChild } from '@angular/core';
import { elementAt } from 'rxjs';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  branches = ["tout", "financier", "commerce", "développement", "etc"];
  catégories = [];



  public onSearch(event : Event){
    event.preventDefault();
    console.log("test");

  }
}
