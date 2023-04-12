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
      }
    );
  }

  deleteGroup(groupCN: string) {

  }
  exportCSV() {
    var csvFile = "cn\n";
    this.listGroups.forEach(group => {
      csvFile += group.cn + "\n";
    });
    var blob = new Blob([csvFile], { type: 'text/csv;charset=utf-8;', endings: 'native' });
    var url = window.URL.createObjectURL(blob);
    window.open(url);


  }
}
