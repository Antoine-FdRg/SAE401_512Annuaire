import { MembersGestionComponent } from './controlPanel/membersGestion/membersGestion.component';
import { ControlPanelComponent } from './controlPanel/controlPanel.component';
import { LoginComponent } from './login/login.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { ResultComponent } from './result/result.component';
const routes: Routes = [

  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
  { path: 'home', component: HomePageComponent },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'controlPanel',
    component: ControlPanelComponent,
  },
  {
    path: 'controlPanel/members',
    component: MembersGestionComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {

}
