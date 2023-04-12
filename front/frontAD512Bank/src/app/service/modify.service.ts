import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { apiURL } from './apiURL';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root'
})
export class ModifyService {
  constructor(private http: HttpClient, private router: Router, private loginService : LoginService) { }

  modifyField(field : string, value : string) {


    let link = apiURL + "/member/" + "modify" + "?attribute=" + field + "&value=" + value;

    this.http.put(link, {}).subscribe(
      () => {
        this.loginService.updateUser();
      }
    );
  }
}
