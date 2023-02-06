import { Component } from '@angular/core';
import { Person } from '../person';
@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent {
  listPerson: Person[] = [];

  constructor(){
    this.listPerson.push({
      surname:'Maïstre',
      name:'Antoine',
      phone:'+33 95 74 24 12',
      email:'truc@machin.com',
      role:'Directeur financier',
      login:'ma102741',
      tutor:'raph@free.fr'
    });
  }

}
