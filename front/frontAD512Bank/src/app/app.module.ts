import { MembersGestionComponent } from './controlPanel/membersGestion/membersGestion.component';
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
import { CommonModule } from '@angular/common';
import { GroupsGestionComponent } from './controlPanel/groups-gestion/groups-gestion.component';
import { InfosComponent } from './infos/infos.component';

@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    ResultComponent,
    ProfileComponent,
    LoginComponent,
    ControlPanelComponent,
    MembersGestionComponent,
    GroupsGestionComponent,
    InfosComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [HttpClient],
  bootstrap: [AppComponent]
})
export class AppModule { }
