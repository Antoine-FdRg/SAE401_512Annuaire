import { Person } from './../../person';
import { AdminService } from './../../service/admin.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-membersGestion',
  templateUrl: './membersGestion.component.html',
  styleUrls: ['./membersGestion.component.css']
})
export class MembersGestionComponent implements OnInit {

  listMembers: Person[] = [];
  constructor(private adminService: AdminService) {
    this.adminService.getMembers().subscribe(
      (response) => {
        this.listMembers = response as Person[];
      }
    );
    this.listMembers.push(
      {
        "firstName": "Raphael",
        "lastName": "Caldwell",
        "email": "test",
        "login": "test",
        "title": "Directeur Commercial",
      }
    )
  }

  ngOnInit() {
  }


}
