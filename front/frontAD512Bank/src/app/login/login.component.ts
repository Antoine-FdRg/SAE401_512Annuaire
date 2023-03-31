import { LoginService } from './../service/login.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {


  constructor(private loginService: LoginService) { }

  connect() {
    var login = (<HTMLInputElement>document.getElementById("userlogin")).value;
    var password = (<HTMLInputElement>document.getElementById("userpassword")).value;
    this.loginService.connect(login, password);
    // TODO : Gérer peut etre le remember me
  }
}
