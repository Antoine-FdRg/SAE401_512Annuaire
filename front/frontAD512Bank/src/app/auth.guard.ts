import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from './service/login.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private loginService: LoginService) { }
  canActivate(): boolean {
    // si l'utilisateur n'est pas connecté, on le redirige vers la page de login
    if (this.loginService.getUser() == undefined) {
      return false;


    }
    if (this.loginService.getUser()!.admin == false) {

      return false;
    }
    else { // si l'utilisateur est connecté, on lui laisse accéder à la page
      return true;
    }

  }

}
