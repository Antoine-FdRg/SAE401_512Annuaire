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
    loginService.updateUser();
    this.user = loginService.user;
  }

  newField(field : ChangingField, element : string = "placeholder"){
    switch (field){
      case ChangingField.address :
        this.modifyservice.modifyField("streetAddress", element);
        this.modified.address = false;
        break;
      case ChangingField.professionalPhone :
        this.modifyservice.modifyField("telephoneNumber", element);
        this.modified.professionalPhone = false;
        break;
      case ChangingField.personalPhone :
        this.modifyservice.modifyField("mobile", element);
        this.modified.personalPhone = false;
        break;
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
