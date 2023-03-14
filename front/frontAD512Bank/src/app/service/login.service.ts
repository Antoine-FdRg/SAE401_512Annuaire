import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  userBase: any = {
    login:"ma102741",
    surname:"Maïstre",
    name:"Antoine",
  }
  constructor() { }
}
