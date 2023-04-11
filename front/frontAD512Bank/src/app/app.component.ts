import { Router, RouterOutlet } from '@angular/router';
import { LoginService } from './service/login.service';
import { SearchService } from './service/search.service';
import { Component } from '@angular/core';
import { fader } from './route-animations';

@Component({
  selector: 'app-root',
  animations: [ // <-- add your animations here
    fader,
    // slider,
    // transformer,
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontAD512Bank';
  constructor(private searchService: SearchService, private router: Router) { }
  public showResults(): boolean {
    return this.searchService.resultShowing;
  }

  public hideResults(): void {
    this.searchService.resultShowing = false;
  }
  public isAdmin(): boolean {
    const url = this.router.url;
    if (url === '/controlPanel') {
      return true;
    }
    return false;
  }
  prepareRoute(outlet: RouterOutlet) {

    return outlet && outlet.activatedRouteData;
  }

}
