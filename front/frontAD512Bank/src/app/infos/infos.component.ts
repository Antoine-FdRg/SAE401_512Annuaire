import { Component } from '@angular/core';
import { LoginService } from '../service/login.service';
import { Person } from '../person';
import { ModifyService } from '../service/modify.service';

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
    "personalPhone"   : false,
    "professionalPhone" : false,
    "birthDate" : false,
  };

  ChangingField = ChangingField;
  user : Person | undefined;

  constructor(public modifyservice : ModifyService, public loginService : LoginService){
    this.user = loginService.user;
  }

  newField(field : ChangingField, value : string = "test"){
    switch (field){
      case ChangingField.address :
        this.modifyservice.modifyField("address", value);
    }
  }
  
}

enum ChangingField {
  surname,
  name,
  address,
  phone,
  mail,
  personalPhone,
  professionalPhone,
  birthDate,
}
