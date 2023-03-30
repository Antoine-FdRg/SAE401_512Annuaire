import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { apiURL } from './apiURL';
@Injectable({
  providedIn: 'root'
})
export class LoginService {


  userBase: any = {
    login: "ma102741",
    surname: "Maïstre",
    name: "Antoine",
  }
  constructor(private http: HttpClient) { }

  connect(login: string, password: string) {
    const httpOptions: any = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*'
      })
    };

    this.http.post(apiURL + "/auth/login", { login: login, password: password }, httpOptions).subscribe(
      (response) => {
        console.log(response);
        this.userBase = response;
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
