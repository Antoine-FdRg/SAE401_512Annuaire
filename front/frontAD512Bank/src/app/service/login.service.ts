import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { apiURL } from './apiURL';
import { Person } from '../person';
@Injectable({
  providedIn: 'root'
})
export class LoginService {


  userBase: Person | undefined;
  constructor(private http: HttpClient, private router: Router) { }

  getUser(): Person | undefined {
    // if userbase undefined then search in session storage
    if (this.userBase == undefined) {
      var parsed = JSON.parse(sessionStorage.getItem('user') || 'null');

      this.userBase = parsed;
      if (this.userBase == null) {
        return undefined
      }
      else {
        return this.userBase;
      }
    }
    else {
      return this.userBase;
    }
  }

  connect(login: string, password: string) {
    // add Access-Control-Allow-Headers: *
    var headers = new HttpHeaders();
    headers = headers.append('Access-Control-Allow-Headers', '*');


    this.http.post(apiURL + "/auth/login", { username: login, password: password }).subscribe(
      (response: any) => {
        this.userBase = { firstName: response.firstName, lastName: response.lastName, login: response.login, email: response.email, dn: response.dn, admin: response.admin };
        sessionStorage.setItem('user', JSON.stringify(this.userBase));
        // store the token Autorisation which is in header in session storage
        sessionStorage.setItem('token', response.token);
        console.log(response);

        this.router.navigate(['/controlPanel']);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  logout() {
    this.http.post(apiURL + "/auth/logout", {}).subscribe(
      (response) => {
        console.log(response);
        sessionStorage.removeItem('user');
        sessionStorage.removeItem('token');
        this.userBase = undefined;
        this.router.navigate(['/login']);
      }
    );

  }
}
