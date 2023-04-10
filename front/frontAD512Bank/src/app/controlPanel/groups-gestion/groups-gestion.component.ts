import { Component } from '@angular/core';
import { AdminService } from 'src/app/service/admin.service';

@Component({
  selector: 'app-groups-gestion',
  templateUrl: './groups-gestion.component.html',
  styleUrls: ['./groups-gestion.component.css']
})
export class GroupsGestionComponent {
  listGroups: { cn: string }[] = []; //TODO : Change type to Group

  constructor(private adminService: AdminService) {
    this.adminService.getGroups().subscribe(
      (response) => {
        this.listGroups = response as [];
        console.log(this.listGroups);
      }
    );
  }
}
