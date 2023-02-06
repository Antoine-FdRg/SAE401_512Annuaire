import { Component } from '@angular/core';
import { Person } from '../person';
@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent {
  listPerson: Person[] = [];

  constructor() {
    this.listPerson.push(
      {
        surname: 'Maïstre',
        name: 'Antoine',
        phone: '+33 95 74 24 12',
        email: 'truc@machin.com',
        role: 'Directeur financier',
        login: 'ma102741',
        tutor: 'raph@free.fr'
      },
      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raphelCon@raphael.con',
        role: 'Con',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      }


    );


  }



}
