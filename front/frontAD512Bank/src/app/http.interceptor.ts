import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {

  constructor() { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = sessionStorage.getItem("token");
    console.log("token: " + token);

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: token
        }
      });
      console.log(request);

    }
    return next.handle(request);
  }
}
