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


    this.http.post(apiURL + "/auth/login", { username: login, password: password }, { observe: "response", headers: headers }).subscribe(
      (response) => {
        this.userBase = response as unknown as Person;
        sessionStorage.setItem('user', JSON.stringify(this.userBase));
        // store the token Autorisation which is in header in session storage
        sessionStorage.setItem('token', response.headers.get('Authorization') || '');
        console.log(response);

        this.router.navigate(['/controlPanel']);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  logout() {
    sessionStorage.removeItem('user');
    this.userBase = undefined;
    this.router.navigate(['/login']);

    this.http.post(apiURL + "/auth/logout", {}, { withCredentials: true }).subscribe(
      (response) => {
        console.log(response);
      }
    );

  }
}
