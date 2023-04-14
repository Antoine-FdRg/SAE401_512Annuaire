import { Component, TemplateRef, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Route } from '@angular/router';
import { Person } from 'src/app/person';
import { AdminService } from 'src/app/service/admin.service';

@Component({
  selector: 'app-members-of-group',
  templateUrl: './members-of-group.component.html',
  styleUrls: ['./members-of-group.component.css']
})
export class MembersOfGroupComponent {
  listMembers: Person[] = []; //TODO : Change type to Group
  cn:string = "";
  targetedGroupCN:String = "";
  deleteDN:string ="";
  constructor(private adminService : AdminService, private route: ActivatedRoute,public confirmationPopup: MatDialog) {



  }
  ngOnInit():void{
    this.route.queryParams.subscribe(params => {
      this.cn = params["cn"];
      this.getMembersOfGroup( this.cn );

    })
  }


  getMembersOfGroup(groupCN: string) {
    this.adminService.getMembersOfGroup(groupCN).subscribe((data)=>{
      this.listMembers=data as [];

      this.sort();

    });
  }
  sort()
  {


    this.listMembers.sort((a,b)=>{
      if(b.title == "Employé" && a.title == "Employé")
      {
        return 0;
      }
      else if(b.title == "Employé" && a.title != "Employé") return -1;
      else
      {
        return 1;
      }
    });
  }
  openDeleteConfirmationPopup(templateRef: TemplateRef<any>, groupCN: String, dn:any) {
    this.confirmationPopup.open(templateRef);
    this.targetedGroupCN = groupCN;
    this.deleteDN = dn;
  }
  cancel()
  {
    this.confirmationPopup.closeAll();
    this.targetedGroupCN = "";
  }
  removeMember()
  {
    if(this.deleteDN != "")
    {
      this.adminService.removeMemberFromGroup(this.cn,this.deleteDN).subscribe((data)=>{
        console.log(data);
        this.ngOnInit();
      });
    }

  }
}
