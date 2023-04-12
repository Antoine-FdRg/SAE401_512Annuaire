import { MatDialog } from '@angular/material/dialog';
import { Component,TemplateRef } from '@angular/core';
import { AdminService } from 'src/app/service/admin.service';

@Component({
  selector: 'app-groups-gestion',
  templateUrl: './groups-gestion.component.html',
  styleUrls: ['./groups-gestion.component.css']
})
export class GroupsGestionComponent {
  listGroups: { cn: string }[] = []; //TODO : Change type to Group

  constructor(private adminService: AdminService,private confirmationPopup: MatDialog) {
    this.adminService.getGroups().subscribe(
      (response) => {
        this.listGroups = response as [];
      }
    );
  }

  openDeleteConfirmationPopup(templateRef: TemplateRef<any>) {
    this.confirmationPopup.open(templateRef);
  }



  deleteGroup(groupCN: string) {
    if (window.confirm("Voulez-vous vraiment supprimer le groupe " + groupCN + " ? Cette action est IRRÉVERSIBLE.")) {
      this.adminService.deleteGroup(groupCN).subscribe((response) => {
        console.log(response);
        this.listGroups = this.listGroups.filter((group) => group.cn != groupCN);
      },
        (error) => {
          console.log(error.status);
          if (error.status == 200) {
            this.popup("Le groupe a bien été supprimé.");
            this.listGroups = this.listGroups.filter((group) => group.cn != groupCN);
          } else if (error.status == 404) {
            this.popup("Le groupe n'existe pas.");
          } else {
            this.popup("Une erreur est survenue lors de la création du groupe.")
          }
        });
    }
  }

  popup(msg: String) {
    //TODO : replace with a better popup
    window.alert(msg);
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
