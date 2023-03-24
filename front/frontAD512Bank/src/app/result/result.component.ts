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
        login: 'ma102748',
        tutor: 'raph@free.fr',
        selected: false
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable comptabilité',
        login: 'con102748',
        tutor: 'antoine.maistre@etu.unice.fr',
        selected: false
      },

      
    );
  }

  personClicked(person : Person){
    console.log("click"); 
    person.selected=true;
  }
}

/*
      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Directeur financier',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable de paie',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable juridique',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },



      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable administration',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable adjoint juridique',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Directeur ressources humaines',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable gestion du personnel',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable recrutement',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Directrice technique',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable logistique',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable recherche et développement',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Directeur commercial',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable produit',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },

      {
        surname: 'Caldwell',
        name: 'Raphaël',
        phone: '+33 95 74 24 12',
        email: 'raph@raphael.con',
        role: 'Responsable marketing',
        login: 'con102741',
        tutor: 'antoine.maistre@etu.unice.fr'
      },
      */