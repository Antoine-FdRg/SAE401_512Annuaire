import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { apiURL } from './apiURL';
import { Person } from '../person';
@Injectable({
  providedIn: 'root'
})
export class LoginService {


  userBase: Person = {
    login: "ma102741",
    firstName: "Maïstre",
    lastName: "Antoine",
    admin: true
  }
  constructor(private http: HttpClient, private router: Router) { }

  connect(login: string, password: string) {
    const httpOptions: any = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*'
      })
    };

    this.http.post(apiURL + "/auth/login", { username: login, password: password }, httpOptions).subscribe(
      (response) => {
        console.log(response);
        this.userBase = {
          login: "ma102741",
          firstName: "Maïstre",
          lastName: "Antoine",
          admin: true
        };
        this.router.navigate(['/controlPanel']);
        console.log(this.userBase);

      },
      (error) => {
        console.log(error);
      }
    );
  }
}
