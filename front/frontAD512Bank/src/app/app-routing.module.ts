import { GroupsGestionComponent } from './controlPanel/groups-gestion/groups-gestion.component';
import { MembersGestionComponent } from './controlPanel/membersGestion/membersGestion.component';
import { ControlPanelComponent } from './controlPanel/controlPanel.component';
import { LoginComponent } from './login/login.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { ResultComponent } from './result/result.component';
import { InfosComponent } from './infos/infos.component';
import { AuthGuard } from './auth.guard';
import { AddGroupComponent } from './controlPanel/add-group/add-group.component';
import { MembersOfGroupComponent } from './controlPanel/members-of-group/members-of-group.component';
const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
  { path: 'home', component: HomePageComponent, },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'controlPanel',
    component: ControlPanelComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'controlPanel/members',
    component: MembersGestionComponent,
    canActivate: [AuthGuard]
  }, {
    path: 'controlPanel/groups',
    component: GroupsGestionComponent,
    canActivate: [AuthGuard],
  }, {
    path: 'infos',
    component: InfosComponent,
  },
  {
    path: 'controlPanel/groups/addGroup',
    component: AddGroupComponent,
    canActivate: [AuthGuard],
  }, {
    path: 'controlPanel/groups/members',
    component: MembersOfGroupComponent,
    canActivate: [AuthGuard],
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {

}
