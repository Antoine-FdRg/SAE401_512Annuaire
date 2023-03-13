import { ApplicationRef, Component, ElementRef, Injector, ViewChild } from '@angular/core';
import { elementAt } from 'rxjs';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  branches = ["tout", "financier", "commerce", "développement", "etc"];
  catégories = [];


  // todo : déclarer un service pour setup les variables de l'utilisateur à afficher (nom, initiales)

  // appElementRef: ElementRef;
  // constructor(private applicationRef:ApplicationRef, injector: Injector) {
  //   this.appElementRef = injector.get(applicationRef.componentTypes[0]).elementRef;

  //   let rootElement : HTMLElement = this.appElementRef.nativeElement as HTMLElement;
  //   rootElement.style.setProperty('--initials', initials);
  // }


  public onSearch(event : Event){
    event.preventDefault();
    console.log("test");

  }
}
