import { FormControl } from '@angular/forms';
import { AdminService } from 'src/app/service/admin.service';
import { Component } from '@angular/core';
import { Person } from 'src/app/person';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-member-to-group',
  templateUrl: './add-member-to-group.component.html',
  styleUrls: ['./add-member-to-group.component.css']
})
export class AddMemberToGroupComponent {
  public listMembers:Person[] = [];
  public person : FormControl= new FormControl("");
  private cn:string = "";

  constructor(private adminService:AdminService, private route: ActivatedRoute)
  {
    this.route.queryParams.subscribe(params => {
      this.cn = params["cn"];
    })
    this.adminService.searchAll().subscribe((data)=>{
      this.listMembers=data as [];
    });
  }
  addMember()
  {
    console.log(this.person.value);
    this.adminService.AddMemberToGroup(this.person.value,this.cn).subscribe((data)=>{
      console.log(data);
    });

  }
}
