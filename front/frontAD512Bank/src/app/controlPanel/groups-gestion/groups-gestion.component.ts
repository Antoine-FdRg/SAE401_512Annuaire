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
  targetedGroupCN:String = "";

  constructor(private adminService: AdminService,private confirmationPopup: MatDialog) {
    this.adminService.getGroups().subscribe(
      (response) => {
        this.listGroups = response as [];
        this.sort();
      });
  }

  openDeleteConfirmationPopup(templateRef: TemplateRef<any>, groupCN: String) {
    this.confirmationPopup.open(templateRef);
    this.targetedGroupCN = groupCN;
  }

  //TODO pq les requetes parte 2 fois ?

  deleteGroup() {
    this.adminService.deleteGroup(this.targetedGroupCN).subscribe((response) => {
      this.listGroups = this.listGroups.filter((group) => group.cn != this.targetedGroupCN);
    },
    (error) => {
      let success:boolean = error.status == 200 || error.status == 201;
      this.popup(success, success?error.error.message:error.error.error);
      if(success){
        this.listGroups = this.listGroups.filter((group) => group.cn != this.targetedGroupCN);
      }
    });
  }

  cancel(){
    this.confirmationPopup.closeAll();
    this.targetedGroupCN = "";
  }

  popup(sucess:boolean, msg: string) {
    let popup:HTMLElement;
    let content:HTMLElement
    if(sucess){
      popup = document.getElementById("popupSuccess")!;
      content= document.getElementById("popupSuccessContent")!;
    }else{
      popup = document.getElementById("popupError")!;
      content = document.getElementById("popupErrorContent")!;
    }
    content.innerHTML = msg;
    popup.style.display = "flex";
    setTimeout(() => {
      popup.classList.add("fadeout");
    }, 1500);
    setTimeout(() => {
      popup.classList.remove("fadeout");
      popup.style.display = "none";
    },3000);
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

  sort()
  {

    this.listGroups.reverse();
  }
}
