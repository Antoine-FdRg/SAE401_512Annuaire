import { Component } from '@angular/core';
import { LoginService } from '../service/login.service';
import { Person } from '../person';

@Component({
  selector: 'app-infos',
  templateUrl: './infos.component.html',
  styleUrls: ['./infos.component.css']
})

export class InfosComponent {
  modified = {
    "address" : false,
    "name"    : false,
    "mail"    : false,
    "surname" : false,
    "phone"   : false,
  };

  ChangingField = ChangingField;
  user : Person | undefined;

  constructor(public loginService : LoginService){
    this.user = loginService.getUser();
  }


  newField(field : ChangingField){
    switch (field){
      case ChangingField.address :
        break;
    }
  }
  
}

enum ChangingField {
  surname,
  name,
  address,
  phone,
  mail
}