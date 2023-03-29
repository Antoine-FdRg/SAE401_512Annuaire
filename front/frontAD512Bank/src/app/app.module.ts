import { HttpClient, HttpClientModule, HttpHandler } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomePageComponent } from './home-page/home-page.component';
import { ResultComponent } from './result/result.component';
import { ProfileComponent } from './home-page/profile/profile.component';
import { LoginComponent } from './login/login.component';
import { ControlPanelComponent } from './controlPanel/controlPanel.component';

@NgModule({
  declarations: [	
    AppComponent,
    HomePageComponent,
    ResultComponent,
    ProfileComponent,
    LoginComponent,
      ControlPanelComponent
   ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [HttpClient],
  bootstrap: [AppComponent]
})
export class AppModule { }
