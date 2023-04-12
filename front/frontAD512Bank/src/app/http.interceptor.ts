import { Injectable, TemplateRef, ViewChild } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AppComponent } from './app.component';
import { AlertComponentComponent } from './login/alert-component/alert-component.component';
import { LoginService } from './service/login.service';
@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {

  constructor(private router: Router, private confirmationPopup: MatDialog, private loginService: LoginService) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = sessionStorage.getItem("token");
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: token
        }
      });
    }
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.loginService.logout();
          this.router.navigate(['/login']);
          this.confirmationPopup.open(AlertComponentComponent, {
            height: '200px',
            width: '300px',
            position: { right: '10px', top: '10px' }
          });
          return throwError(error); // utiliser throwError pour renvoyer une erreur
        }
        return throwError(error);
      })
    );
  }
}
