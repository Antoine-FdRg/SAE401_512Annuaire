import { SearchService } from './service/search.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontAD512Bank';
  constructor(private searchService: SearchService) { }
  public showResults(): boolean {
    return this.searchService.resultShowing;
  }
}
