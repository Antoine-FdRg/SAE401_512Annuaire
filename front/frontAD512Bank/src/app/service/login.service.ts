import {Router} from '@angular/router';
import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {apiURL} from './apiURL';
import {Person} from '../person';
interface LoginResponse {
  firstName: string;
  lastName: string;
  login: string;
  email: string;
  dn: string;
  admin: boolean;
}
@Injectable({
  providedIn: 'root'
})

export class LoginService {


  userBase: Person | undefined;
  public user : Person = {
    firstName: '',
    lastName: '',
    login: ''
  };
  constructor(private http: HttpClient, private router: Router) { }


  getUser(): Person | undefined {
    // if userbase undefined then search in session storage
    if (this.userBase == undefined) {
      var parsed = JSON.parse(sessionStorage.getItem('user') || 'null');

      this.userBase = parsed;
      if (this.userBase == null) {
        return undefined
      } else {
        return this.userBase;
      }
    } else {
      return this.userBase;
    }
  }

  async loginConfirmationAndAdmin(login: string, password: string): Promise<boolean> {
    var headers = new HttpHeaders();
    headers = headers.append('Access-Control-Allow-Headers', '*');
    try {
      var response = await this.http.post<LoginResponse>(apiURL + "/auth/login", {username: login, password: password}).toPromise();
      if (response !== undefined){
        this.userBase = {
          firstName: response.firstName,
          lastName: response.lastName,
          login: response.login,
          email: response.email,
          dn: response.dn,
          admin: response.admin
        };
        return !!this.userBase.admin;
      } else {
        return false;
      }
    } catch (e) {
      console.log(e);
      return false;
    }
  }

  connect(login: string, password: string) {
    // add Access-Control-Allow-Headers: *
    var headers = new HttpHeaders();
    headers = headers.append('Access-Control-Allow-Headers', '*');

    this.http.post(apiURL + "/auth/login", { username: login, password: password }).subscribe(
      (response: any) => {
        this.userBase = { 
          firstName: response.firstName, 
          lastName: response.lastName, 
          login: response.login, 
          email: response.email, 
          dn: response.dn, 
          admin: response.admin };

        sessionStorage.setItem('user', JSON.stringify(this.userBase));
        // store the token Autorisation which is in header in session storage
        sessionStorage.setItem('token', response.token);
        console.log(response);

        this.updateUser();
        this.router.navigate(['/controlPanel']);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  updateUser() {
    this.http.get(apiURL + "/member/getInfo", {}).subscribe(
      (response : any) => {
        console.log(response);
        this.user.firstName = response.firstName;
        this.user.lastName = response.lastName;
        this.user.login = response.login;
        this.user.email = response.email;
        this.user.address = response.address;
        this.user.personalPhone = response.personalPhone;
        this.user.professionalPhone = response.professionalPhone;
        this.user.dn = response.dn;
        this.user.admin = response.admin;
        this.user.birthDate = response.dateOfBirth;
      }
    );
  }

  logout() {
    this.http.post(apiURL + "/auth/logout", {}).subscribe(
      (response) => {
        sessionStorage.removeItem('user');
        sessionStorage.removeItem('token');
        this.userBase = undefined;
        this.router.navigate(['/login']);
      }
    );

  }
}
