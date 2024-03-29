import { MembersGestionComponent } from './controlPanel/membersGestion/membersGestion.component';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule, HttpHandler } from '@angular/common/http';
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
import { HttpRequestInterceptor } from './http.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SudoPopupComponent } from './controlPanel/sudo-popup/sudo-popup.component';
import { CreateUserFormComponent } from './controlPanel/membersGestion/create-user-form/create-user-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AddGroupComponent } from './controlPanel/add-group/add-group.component';
import { MembersOfGroupComponent } from './controlPanel/members-of-group/members-of-group.component';
import { MatDialogModule } from '@angular/material/dialog';
import { AlertComponentComponent } from './login/alert-component/alert-component.component';
import { AddMemberToGroupComponent } from './controlPanel/add-member-to-group/add-member-to-group.component';
import { OrganigrammeComponent } from './organigramme/organigramme.component';
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
    InfosComponent,
    AddGroupComponent,
    SudoPopupComponent,
    CreateUserFormComponent,
    AlertComponentComponent,
    MembersOfGroupComponent,
    AddMemberToGroupComponent,
    OrganigrammeComponent
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatDialogModule
  ],
  providers: [HttpClient,
    {
      provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
