import { Component } from '@angular/core';
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
  constructor(private adminService : AdminService, private route: ActivatedRoute) {
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
}
